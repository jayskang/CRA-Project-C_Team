package command;

import shell.ISsdCommand;

import java.io.IOException;

public class WriteCommand extends AbstractCommand {
    public WriteCommand(ISsdCommand ssdTestShell) {
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

        try {
            ssdTestShell.write(args[1], args[2]);
        } catch (IllegalArgumentException | IOException e) {
            logger.print(e.getMessage());
        }
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length < 3;
    }
}
