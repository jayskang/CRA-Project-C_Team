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
import java.util.Optional;

import static cores.CommandBufferConstraint.*;
import static cores.SSDConstraint.RESULT_FILENAME;

public class Buffer extends SSDCommonUtils implements BufferCore {

    private final ArrayList<Commander> commanders;
    private boolean[] dirty;

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

    private void reschedule(Commander newCommand) {

        if (this.commanders.isEmpty()) {
            if (newCommand.getCommand().equals(Commander.WRITE)) {
                this.dirty[newCommand.getLba()] = true;
            }
            this.commanders.add(newCommand);
            return;
        }
        String newCmdType = newCommand.getCommand();

        if (newCmdType.equals(Commander.WRITE)) {
            Commander latestCmd = this.commanders.get(this.commanders.size() - 1);

            if (latestCmd.getCommand().equals(Commander.WRITE)) {
                if (this.dirty[newCommand.getLba()]) {
                    this.commanders.removeIf(commander -> commander.getLba() == newCommand.getLba());
                }
            } else if (latestCmd.getCommand().equals(Commander.ERASE)) {
                Commander eraseCmd = this.commanders.remove(this.commanders.size() - 1);

                int baseLba = newCommand.getLba();
                int eraseStartLba = eraseCmd.getLba();
                int size = Integer.parseInt(eraseCmd.getInputData());

                if ((eraseStartLba <= baseLba && baseLba < (eraseStartLba + size))) {

                    if (baseLba == eraseStartLba) {
                        reschedule(createCommand(Commander.ERASE, String.valueOf(baseLba + 1), String.valueOf(size - 1)));
                    } else if (baseLba == (eraseStartLba + size - 1)) {
                        reschedule(createCommand(Commander.ERASE, String.valueOf(baseLba - 1), String.valueOf(size - 1)));
                    } else {
                        Commander newE1 = createCommand(Commander.ERASE, String.valueOf(baseLba + 1), String.valueOf(size - 1));
                        Commander newE2 = createCommand(Commander.ERASE, String.valueOf(baseLba - 1), String.valueOf(size - 1));
                        reschedule(newE1);
                        reschedule(newE2);
                    }
                }
            }
            this.commanders.add(newCommand);
            this.dirty[newCommand.getLba()] = true;
        } else {
            // TODO Erase 처리
            this.commanders.add(newCommand);
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
}
