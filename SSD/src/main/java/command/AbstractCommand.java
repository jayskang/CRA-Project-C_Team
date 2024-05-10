package command;

public abstract class AbstractCommand implements Command {
    protected String commandString;
    protected int lba = -1;
    protected String inputData;

    public AbstractCommand(String[] args) {
        try {
            commandString = args[0];
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

    @Override
    public int getLba() {
        return lba;
    }

    @Override
    public String getInputData() {
        return inputData;
    }

    @Override
    public String toString() {
        return commandString + " " + lba + " " + inputData;
    }
}
