package command;

import cores.CommandBufferConstraint;
import cores.SSDConstraint;

import java.util.ArrayList;

import static cores.CommandBufferConstraint.*;

public class Buffer {

    private final ArrayList<Commander> commanders;
    private boolean[] dirty;

    private static volatile Buffer instance = null;

    private Buffer() {
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

    public void flush() {
        this.commanders.forEach(Commander::runCommand);
        this.commanders.clear();
    }

    private void reschedule(Commander newCommand) {

        if(this.commanders.isEmpty()) {
            this.dirty[newCommand.getLba()] = true;
            this.commanders.add(newCommand);
            return;
        }
        Commander oldCommand = this.commanders.get(this.commanders.size() - 1);
        String oldCmdType = oldCommand.getCommand();
        String newCmdType = newCommand.getCommand();

        if(oldCmdType.equals("W") && newCmdType.equals("E")) {
            int lba = newCommand.getLba();
            int size = Integer.parseInt(newCommand.getInputData());

            for(int i = lba; i < lba + size; i += 1) {
                if(this.dirty[i]) {
                    int deletedIndex = i;
                    this.commanders.removeIf(candidate -> candidate.getLba() == deletedIndex);
                }
                this.dirty[i] = false;
            }
            this.commanders.add(newCommand);
        }
        else {
            // TODO LBA가 겹치는 경우가 없다
            this.dirty[newCommand.getLba()] = true;
        }
    }

    public void push(Commander command) {
        if(this.commanders.size() == MAX_SIZE) {
            flush();
        }
        reschedule(command);
    }

    public ArrayList<Commander> getCommanders() {
        return commanders;
    }
}
