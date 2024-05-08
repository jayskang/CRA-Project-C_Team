public abstract class AbstractCommand implements Command {
    protected String ERROR_MESSAGE = "";
    protected String HELP_MASSAGE = "";
    protected String[] args;
    protected ISsdTestShell ssdTestShell;

    public AbstractCommand(ISsdTestShell ssdTestShell) {
        this.ssdTestShell = ssdTestShell;
    }

    @Override
    public void setArgument(String[] args) {
        this.args = args;
    }

    @Override
    public abstract void execute();

    @Override
    public void printError() {
        System.out.println(ERROR_MESSAGE);
        printHelpMessage();
    }

    @Override
    public void printHelpMessage() {
        System.out.println(HELP_MASSAGE);
    }

    @Override
    public abstract boolean isInvalidArguments();
}
