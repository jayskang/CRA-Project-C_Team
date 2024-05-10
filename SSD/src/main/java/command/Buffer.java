package command;

import cores.SSDCommonUtils;
import erase.EraseModule;
import read.ReadModule;
import write.WriteModule;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import static cores.CommandBufferConstraint.MAX_SIZE;
import static cores.SSDConstraint.MAX_ERASE_SIZE;
import static cores.SSDConstraint.RESULT_FILENAME;

public class Buffer extends SSDCommonUtils implements BufferCore {

    private final ArrayList<Commander> commanders;

    private static volatile Buffer instance = null;

    private Buffer() {
        super();
        this.commanders = new ArrayList<>(MAX_SIZE);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(BUFFER_FILE_NAME));
            String line;

            while (true) {

                line = bufferedReader.readLine();

                if (line == null) {
                    break;
                }
                String[] data = line.split(" ");
                this.commanders.add(createCommand(data[0], data[1], data[2]));
            }
            bufferedReader.close();
        } catch (IOException ignored) {
        }
    }

    public static Buffer getInstance() {
        if (Buffer.instance == null) {
            synchronized (Buffer.class) {
                if (Buffer.instance == null) {
                    Buffer.instance = new Buffer();
                }
            }
        }
        return Buffer.instance;
    }

    private Commander createCommand(String type, String lba, String value) {
        switch (type) {
            case Commander.WRITE:
                return new Commander(new String[]{type, lba, value},
                        null, new WriteModule(), null);
            case Commander.ERASE:
                return new Commander(new String[]{type, lba, value},
                        null, null, new EraseModule());
            case Commander.READ:
                return new Commander(new String[]{type, lba, value},
                        new ReadModule(), null, null);
        }
        return null;
    }

    private void overwriteCommandByErase(Commander newCommand) {
        int eraseStartLba = newCommand.getLba();
        int size = Integer.parseInt(newCommand.getInputData());

        this.commanders.removeIf(command -> command.getCommand().equals(Commander.WRITE)
                && eraseStartLba <= command.getLba()
                && command.getLba() < (eraseStartLba + size));
    }

    private boolean isMergeErase(Commander target, Commander base) {
        int baseStartLba = base.getLba();
        int baseSize = Integer.parseInt(base.getInputData());
        int baseEndLba = baseStartLba + baseSize - 1;

        int targetStartLba = target.getLba();
        int targetSize = Integer.parseInt(target.getInputData());
        int targetEndLba = targetStartLba + targetSize - 1;

        int mergeSize = Math.max(baseEndLba, targetEndLba) - Math.min(baseStartLba, targetStartLba) + 1;

        if (MAX_ERASE_SIZE < mergeSize) {
            return false;
        }
        return targetEndLba + 1 >= baseStartLba && baseEndLba + 1 >= targetStartLba;
    }

    private void mergeEraseCommands() {
        int until = this.commanders.size();

        for (int i = 0; i < until - 1; i += 1) {
            Commander baseCommand = this.commanders.get(i);

            if (baseCommand.getCommand().equals(Commander.ERASE)) {
                for (int j = i + 1; j < until; j += 1) {
                    Commander targetCommand = this.commanders.get(j);

                    if (targetCommand.getCommand().equals(Commander.ERASE)) {

                        if (isMergeErase(targetCommand, baseCommand)) {

                            this.commanders.remove(i--);
                            this.commanders.remove(targetCommand);
                            until -= 1;

                            int baseStartLba = baseCommand.getLba();
                            int baseSize = Integer.parseInt(baseCommand.getInputData());
                            int baseEndLba = baseStartLba + baseSize - 1;

                            int targetStartLba = targetCommand.getLba();
                            int targetSize = Integer.parseInt(targetCommand.getInputData());
                            int targetEndLba = targetStartLba + targetSize - 1;

                            int mergeLba = Math.min(baseStartLba, targetStartLba);
                            int mergeSize = Math.max(baseEndLba, targetEndLba) - Math.min(baseStartLba, targetStartLba) + 1;

                            this.commanders.add(createCommand(Commander.ERASE,
                                    String.valueOf(mergeLba),
                                    String.valueOf(mergeSize)));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void divideEraseCommand(int baseLba, int eraseStartLba, int size) {
        if (baseLba == eraseStartLba) {
            if (size - 1 > 0) {
                reschedule(createCommand(Commander.ERASE, String.valueOf(baseLba + 1), String.valueOf(size - 1)));
            }
        } else if (baseLba == (eraseStartLba + size - 1)) {
            if (size - 1 > 0) {
                reschedule(createCommand(Commander.ERASE, String.valueOf(baseLba - 1), String.valueOf(size - 1)));
            }
        } else {
            int newSize = eraseStartLba + size - 1;
            Commander newE1 = createCommand(Commander.ERASE, String.valueOf(eraseStartLba), String.valueOf(baseLba - eraseStartLba));
            Commander newE2 = createCommand(Commander.ERASE, String.valueOf(baseLba + 1), String.valueOf(newSize - baseLba));

            reschedule(newE1);
            reschedule(newE2);
        }
    }

    private void checkRearrangeEraseCommand(Commander newCommand) {
        for (int i = this.commanders.size() - 1; i >= 0; i -= 1) {
            Commander candidateCmd = this.commanders.get(i);

            if (candidateCmd.getCommand().equals(Commander.ERASE)) {

                int baseLba = newCommand.getLba();
                int eraseStartLba = candidateCmd.getLba();
                int size = Integer.parseInt(candidateCmd.getInputData());

                if ((eraseStartLba <= baseLba && baseLba < (eraseStartLba + size))) {
                    this.commanders.remove(i);
                    divideEraseCommand(baseLba, eraseStartLba, size);
                    break;
                }
            }
        }
    }

    private void reschedule(Commander newCommand) {
        String newCmdType = newCommand.getCommand();

        if (newCmdType.equals(Commander.WRITE)) {
            if (!this.commanders.isEmpty()) {
                this.commanders.removeIf(commander -> commander.getCommand().equals(Commander.WRITE)
                        && commander.getLba() == newCommand.getLba());
                checkRearrangeEraseCommand(newCommand);
            }
            this.commanders.add(newCommand);
        } else if (newCmdType.equals(Commander.ERASE)) {
            overwriteCommandByErase(newCommand);
            this.commanders.add(newCommand);
            mergeEraseCommands();
        }
    }

    @Override
    public void flush() {
        this.commanders.forEach(commander -> {
            if (commander.getCommand().equals(Commander.WRITE)) {
                commander.setCommand(Commander.FILE_WRITE);
            }
        });
        this.commanders.forEach(Commander::runCommand);
        this.commanders.clear();

        try {
            FileWriter fileWriter = new FileWriter(BUFFER_FILE_NAME, false);
            fileWriter.write("");
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public boolean hit(int lba) {
        Optional<Commander> foundCommand = this.commanders.stream()
                .filter(command -> command.getLba() == lba)
                .findFirst();

        // 결과 출력
        if (foundCommand.isPresent()) {
            Commander findCommand = foundCommand.get();
            this.writeToFile(RESULT_FILENAME, findCommand.getInputData());
            return true;
        }
        return false;
    }

    @Override
    public void push(Commander command) {
        if (this.commanders.size() == MAX_SIZE) {
            flush();
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BUFFER_FILE_NAME, false));
            for (Commander currentCommand : this.commanders) {
                bufferedWriter.write(currentCommand.toString());
                bufferedWriter.write("\n");
            }
            bufferedWriter.close();
        } catch (IOException ignored) {
        }

        reschedule(command);
    }

    public ArrayList<Commander> getCommanders() {
        return commanders;
    }

    public static void resetInstance() {
        Buffer.instance = null;
    }
}
