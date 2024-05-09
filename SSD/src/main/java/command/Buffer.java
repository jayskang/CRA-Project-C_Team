package command;

import cores.CommandBufferConstraint;
import cores.SSDCommonUtils;
import cores.SSDConstraint;
import erase.EraseModule;
import read.ReadModule;
import write.WriteModule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import static cores.CommandBufferConstraint.*;
import static cores.SSDConstraint.MAX_ERASE_SIZE;
import static cores.SSDConstraint.RESULT_FILENAME;

public class Buffer extends SSDCommonUtils implements BufferCore {

    private final ArrayList<Commander> commanders;
    private final boolean[] dirty;

    private static volatile Buffer instance = null;

    private Buffer() {
        super();
        this.commanders = new ArrayList<>(MAX_SIZE);
        this.dirty = new boolean[SSDConstraint.MAX_BOUNDARY];
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

        this.commanders.removeIf(command -> {
            if (command.getCommand().equals(Commander.WRITE)
                    && eraseStartLba <= command.getLba()
                    && command.getLba() < (eraseStartLba + size)) {
                this.dirty[command.getLba()] = false;
                return true;
            }
            return false;
        });
    }

    private boolean isMergeErase(Commander target, Commander base) {
        int baseLba = base.getLba();
        int baseSize = Integer.parseInt(base.getInputData());
        int targetLba = target.getLba();
        int targetSize = Integer.parseInt(target.getInputData());
        int diff = Math.abs((baseLba + baseSize) - (targetLba + targetSize));

        if (MAX_ERASE_SIZE < Math.min(baseSize, targetSize) + diff) {
            return false;
        } else if ((targetLba + targetSize) < baseLba) {
            return false;
        }
        return (baseLba + baseSize) >= targetLba;
    }

    private void mergeEraseCommands() {
        int until = this.commanders.size();

        for (int i = 0; i < until - 1; i += 1) {
            Commander baseCommand = this.commanders.get(i);

            for (int j = i + 1; j < until; j += 1) {
                Commander targetCommand = this.commanders.get(j);

                if (targetCommand.getCommand().equals(Commander.ERASE)) {

                    if (isMergeErase(targetCommand, baseCommand)) {

                        this.commanders.remove(i--);
                        this.commanders.remove(targetCommand);
                        until -= 1;

                        int baseLba = baseCommand.getLba();
                        int baseSize = Integer.parseInt(baseCommand.getInputData());
                        int targetLba = targetCommand.getLba();
                        int targetSize = Integer.parseInt(targetCommand.getInputData());
                        int diff = Math.abs((baseLba + baseSize) - (targetLba + targetSize));

                        this.commanders.add(createCommand(Commander.ERASE,
                                String.valueOf(Math.min(baseLba, targetLba)),
                                String.valueOf(Math.min(baseSize, targetSize) + diff)));
                        break;
                    }
                }
            }
        }
    }

    private void divideEraseCommand(int baseLba, int eraseStartLba, int size) {
        if (baseLba == eraseStartLba) {
            reschedule(createCommand(Commander.ERASE, String.valueOf(baseLba + 1), String.valueOf(size - 1)));
        } else if (baseLba == (eraseStartLba + size - 1)) {
            reschedule(createCommand(Commander.ERASE, String.valueOf(baseLba - 1), String.valueOf(size - 1)));
        } else {
            Commander newE1 = createCommand(Commander.ERASE, String.valueOf(baseLba + 1), String.valueOf(size - 2));
            Commander newE2 = createCommand(Commander.ERASE, String.valueOf(baseLba - 1), String.valueOf(size - 2));
            reschedule(newE1);
            reschedule(newE2);
        }
    }

    private void checkRearrangeEraseCommand(Commander newCommand) {
        Commander eraseCmd = this.commanders.remove(this.commanders.size() - 1);

        int baseLba = newCommand.getLba();
        int eraseStartLba = eraseCmd.getLba();
        int size = Integer.parseInt(eraseCmd.getInputData());

        if ((eraseStartLba <= baseLba && baseLba < (eraseStartLba + size))) {
            divideEraseCommand(baseLba, eraseStartLba, size);
        }
    }

    private void reschedule(Commander newCommand) {
        String newCmdType = newCommand.getCommand();

        if (newCmdType.equals(Commander.WRITE)) {
            if(!this.commanders.isEmpty()) {
                Commander latestCmd = this.commanders.get(this.commanders.size() - 1);

                if (latestCmd.getCommand().equals(Commander.WRITE)) {
                    if (this.dirty[newCommand.getLba()]) {
                        this.commanders.removeIf(commander -> commander.getLba() == newCommand.getLba());
                    }
                } else if (latestCmd.getCommand().equals(Commander.ERASE)) {
                    checkRearrangeEraseCommand(newCommand);
                }
            }
            this.commanders.add(newCommand);
            this.dirty[newCommand.getLba()] = true;
        } else if (newCmdType.equals(Commander.ERASE)) {
            overwriteCommandByErase(newCommand);
            this.commanders.add(newCommand);
            mergeEraseCommands();
        }
    }

    @Override
    public void flush() {
        this.commanders.forEach(Commander::runCommand);
        this.commanders.clear();
    }

    @Override
    public boolean hit(Commander newCommand) {
        Optional<Commander> foundCommand = this.commanders.stream()
                .filter(command -> command.getLba() == newCommand.getLba())
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
        reschedule(command);
    }

    public ArrayList<Commander> getCommanders() {
        return commanders;
    }

    public boolean[] getDirty() {
        return dirty;
    }

    public static void resetInstance() {
        Buffer.instance = null;
    }
}
