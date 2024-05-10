package command;

import read.ReadCore;
import read.ReadModule;

public class ReadCommand extends AbstractCommand {
    public ReadCommand(String[] args) {
        super(args);
    }

    @Override
    protected boolean isValidInputData() {
        return true;
    }

    @Override
    public void executeCommand() {
        ReadCore readCore = new ReadModule();
        readCore.bufferRead(lba);
    }
}
