package command;

import cores.SSDCommonUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import static command.CommandConstant.*;
import static cores.CommandBufferConstraint.MAX_SIZE;
import static cores.SSDConstraint.MAX_ERASE_SIZE;
import static cores.SSDConstraint.RESULT_FILENAME;

public class Buffer extends SSDCommonUtils implements BufferCore {

    private final ArrayList<Command> commands;

    private static volatile Buffer instance = null;

    private Buffer() {
        super();
        this.commands = new ArrayList<>(MAX_SIZE);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(BUFFER_FILE_NAME));
            String line;

            while (true) {

                line = bufferedReader.readLine();

                if (line == null) {
                    break;
                }
                String[] data = line.split(" ");
                this.commands.add(createCommand(data));
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

    private Command createCommand(String[] args) {
        return CommandFactory.getCommand(args);
    }

    private Command createCommand(String commandString, String lba, String inputData) {
        String[] args = new String[]{commandString, lba, inputData};
        return CommandFactory.getCommand(args);
    }

    private void overwriteCommandByErase(Command newCommand) {
        int eraseStartLba = newCommand.getLba();
        int size = Integer.parseInt(newCommand.getInputData());

        this.commands.removeIf(command -> command instanceof WriteCommand
                && eraseStartLba <= command.getLba()
                && command.getLba() < (eraseStartLba + size));
    }

    private boolean isMergeErase(Command target, Command base) {
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
        int until = this.commands.size();

        for (int i = 0; i < until - 1; i += 1) {
            Command baseCommand = this.commands.get(i);

            if (baseCommand instanceof EraseCommand) {
                for (int j = i + 1; j < until; j += 1) {
                    Command targetCommand = this.commands.get(j);

                    if (targetCommand instanceof EraseCommand) {

                        if (isMergeErase(targetCommand, baseCommand)) {

                            this.commands.remove(i--);
                            this.commands.remove(targetCommand);
                            until -= 1;

                            int baseStartLba = baseCommand.getLba();
                            int baseSize = Integer.parseInt(baseCommand.getInputData());
                            int baseEndLba = baseStartLba + baseSize - 1;

                            int targetStartLba = targetCommand.getLba();
                            int targetSize = Integer.parseInt(targetCommand.getInputData());
                            int targetEndLba = targetStartLba + targetSize - 1;

                            int mergeLba = Math.min(baseStartLba, targetStartLba);
                            int mergeSize = Math.max(baseEndLba, targetEndLba) - Math.min(baseStartLba, targetStartLba) + 1;

                            this.commands.add(createCommand(ERASE,
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
                reschedule(createCommand(ERASE, String.valueOf(baseLba + 1), String.valueOf(size - 1)));
            }
        } else if (baseLba == (eraseStartLba + size - 1)) {
            if (size - 1 > 0) {
                reschedule(createCommand(ERASE, String.valueOf(baseLba - 1), String.valueOf(size - 1)));
            }
        } else {
            int newSize = eraseStartLba + size - 1;
            Command newE1 = createCommand(ERASE, String.valueOf(eraseStartLba), String.valueOf(baseLba - eraseStartLba));
            Command newE2 = createCommand(ERASE, String.valueOf(baseLba + 1), String.valueOf(newSize - baseLba));

            reschedule(newE1);
            reschedule(newE2);
        }
    }

    private void checkRearrangeEraseCommand(Command newCommand) {
        for (int i = this.commands.size() - 1; i >= 0; i -= 1) {
            Command candidateCmd = this.commands.get(i);

            if (candidateCmd instanceof EraseCommand) {

                int baseLba = newCommand.getLba();
                int eraseStartLba = candidateCmd.getLba();
                int size = Integer.parseInt(candidateCmd.getInputData());

                if ((eraseStartLba <= baseLba && baseLba < (eraseStartLba + size))) {
                    this.commands.remove(i);
                    divideEraseCommand(baseLba, eraseStartLba, size);
                    break;
                }
            }
        }
    }

    private void reschedule(Command newCommand) {
        if (newCommand instanceof WriteCommand) {
            if (!this.commands.isEmpty()) {
                this.commands.removeIf(command -> command instanceof WriteCommand
                        && command.getLba() == newCommand.getLba());
                checkRearrangeEraseCommand(newCommand);
            }
            this.commands.add(newCommand);
        } else if (newCommand instanceof EraseCommand) {
            overwriteCommandByErase(newCommand);
            this.commands.add(newCommand);
            mergeEraseCommands();
        }
    }

    @Override
    public void flush() {
        this.commands.forEach(Command::executeCommand);
        this.commands.clear();

        try {
            FileWriter fileWriter = new FileWriter(BUFFER_FILE_NAME, false);
            fileWriter.write("");
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public boolean hit(int lba) {
        Optional<Command> foundCommand = this.commands.stream()
                .filter(command -> command.getLba() == lba)
                .findFirst();

        // 결과 출력
        if (foundCommand.isPresent()) {
            Command findCommand = foundCommand.get();
            this.writeToFile(RESULT_FILENAME, findCommand.getInputData());
            return true;
        }
        return false;
    }

    @Override
    public void push(Command command) {
        if (this.commands.size() == MAX_SIZE) {
            flush();
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BUFFER_FILE_NAME, false));
            for (Command currentCommand : this.commands) {
                bufferedWriter.write(currentCommand.toString());
                bufferedWriter.write("\n");
            }
            bufferedWriter.close();
        } catch (IOException ignored) {
        }

        reschedule(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public static void resetInstance() {
        Buffer.instance = null;
    }
}
