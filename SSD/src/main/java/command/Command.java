package command;

public interface Command {
    boolean isValidArgument();
    void executeCommand();
    void executeBuffer();
    int getLba();
    String getInputData();
}
