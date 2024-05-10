package command;

public interface Command {
    boolean isValidArgument();
    void executeCommand();
    int getLba();
    String getInputData();
}
