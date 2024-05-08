public class WriteCommand extends AbstractCommand {
    public WriteCommand(ISsdTestShell ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "Write need LBA and data.";
        HELP_MASSAGE = "Usage: write [LBA] [data]";
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        ssdTestShell.write(args[1], args[2]);
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length < 3;
    }
}
