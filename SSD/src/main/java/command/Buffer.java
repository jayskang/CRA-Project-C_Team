package command;

import cores.CommandBufferConstraint;
import cores.SSDCommonUtils;
import cores.SSDConstraint;

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

    private void reschedule(Commander newCommand) {

        if (this.commanders.isEmpty()) {
            this.dirty[newCommand.getLba()] = true;
            this.commanders.add(newCommand);
            return;
        }
        String newCmdType = newCommand.getCommand();

        if (newCmdType.equals("W")) {
            if (this.dirty[newCommand.getLba()]) {
                this.commanders.removeIf(commander -> commander.getLba() == newCommand.getLba());
            }
            this.commanders.add(newCommand);
            this.dirty[newCommand.getLba()] = true;
        } else {
            // TODO Erase 처리
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
