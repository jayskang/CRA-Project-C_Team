package command;

import erase.EraseCore;
import erase.EraseModule;

public class EraseCommand extends AbstractCommand {
    public EraseCommand(String[] args) {
        super(args);
    }

    @Override
    public void executeCommand() {
        EraseCore eraseCore = new EraseModule();
        try {
            eraseCore.erase(lba, Integer.parseInt(inputData));
        } catch (NumberFormatException e) {
            return;
        }
    }

    @Override
    protected boolean isValidInputData() {
        try {
            Integer.parseInt(inputData);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
