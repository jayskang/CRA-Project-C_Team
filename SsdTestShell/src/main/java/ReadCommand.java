public class ReadCommand extends AbstractCommand {
    public ReadCommand(ISsdTestShell ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "Write need LBA.";
        HELP_MASSAGE = "Usage: read [LBA]";
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        ssdTestShell.read(args[1]);
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length < 2;
    }
}
