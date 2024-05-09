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
        String newCmdType = newCommand.getCommand();

        if(newCmdType.equals("W")) {
            if(this.dirty[newCommand.getLba()]) {
                this.commanders.removeIf(commander -> commander.getLba() == newCommand.getLba());
            }
            this.commanders.add(newCommand);
            this.dirty[newCommand.getLba()] = true;
        }
        else {
            // TODO Erase 일 때
            this.commanders.add(newCommand);
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
