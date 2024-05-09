package command;

import shell.ISsdCommand;

import static java.lang.System.exit;

public class ExitCommand extends AbstractCommand {
    protected ExitCommand(ISsdCommand ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "Exit command need no arguments. Please check Input.";
        HELP_MASSAGE = "Usage: exit";
    }

    @Override
    public void execute() {
        if (isInvalidArguments()) {
            printError();
            return;
        }

        exit(0);
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length > 1;
    }
}
