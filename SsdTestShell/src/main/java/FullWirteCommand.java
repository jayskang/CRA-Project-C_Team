public class FullWirteCommand extends AbstractCommand {
    public FullWirteCommand(ISsdTestShell ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "Fullwrite need data.";
        HELP_MASSAGE = "Usage: fullwrite [data]";
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        ssdTestShell.fullwrite(args[1]);
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length < 2;
    }
}
