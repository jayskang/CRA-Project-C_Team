public interface Command {
    void execute();
    void printError();
    boolean isInvalidArguments();
    void setArgument(String[] args);
    void printHelpMessage();
}
