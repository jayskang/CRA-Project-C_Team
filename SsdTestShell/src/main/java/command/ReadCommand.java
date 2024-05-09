package command;

import shell.ISsdCommand;

import java.io.IOException;

public class ReadCommand extends AbstractCommand {
    public ReadCommand(ISsdCommand ssdTestShell) {
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

        try {
            System.out.println(ssdTestShell.read(args[1]));
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length < 2;
    }
}
