package command;

public abstract class AbstractCommand implements Command {
    protected int lba = -1;
    protected String inputData;

    public AbstractCommand(String[] args) {
        try {
            lba = Integer.parseInt(args[1]);
            inputData = args[2];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return;
        }
    }

    @Override
    public boolean isValidArgument() {
        return isValidLba() && isValidInputData();
    };

    @Override
    public abstract void executeCommand();

    protected boolean isValidLba() {
        return lba != -1;
    }

    protected boolean isValidInputData() {
        return inputData != null && !inputData.isEmpty();
    }
}
