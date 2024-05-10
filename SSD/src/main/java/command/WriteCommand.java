package command;

import write.WriteCore;
import write.WriteModule;

public class WriteCommand extends AbstractCommand {
    public WriteCommand(String[] args) {
        super(args);
    }

    @Override
    public void executeCommand() {
        WriteCore writeCore = new WriteModule();
        writeCore.write(lba, inputData);
    }
}
