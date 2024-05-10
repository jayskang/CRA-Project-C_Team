package command;

public class FlushCommand extends AbstractCommand {
    public FlushCommand(String[] args) {
        super(args);
    }

    @Override
    public void executeCommand() {
        Buffer.getInstance().flush();
    }

    @Override
    protected boolean isValidInputData() {
        return true;
    }

    @Override
    protected boolean isValidLba() {
        return true;
    }
}
