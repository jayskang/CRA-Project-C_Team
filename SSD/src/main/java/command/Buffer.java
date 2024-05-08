package command;

import cores.CommandBufferConstraint;

import java.util.ArrayList;

import static cores.CommandBufferConstraint.*;

public class Buffer {

    private final ArrayList<Commander> commanders;

    private static volatile Buffer instance = null;

    private Buffer() {
        this.commanders = new ArrayList<>(MAX_SIZE);
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

    private void reschedule(Commander command) {

    }

    public void push(Commander command) {
        if(this.commanders.size() == MAX_SIZE) {
            flush();
        }
        reschedule(command);
    }
}
