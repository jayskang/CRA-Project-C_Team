package command;

public interface Command {
    boolean isValidArgument();
    void executeCommand();
}
