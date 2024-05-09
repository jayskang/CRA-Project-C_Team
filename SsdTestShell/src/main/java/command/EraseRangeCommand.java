package command;

import java.io.IOException;

public class EraseRangeCommand extends AbstractCommand {
    public EraseRangeCommand(ISsdCommand ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "erase_range need start lba and end lba.";
        HELP_MASSAGE = "Usage: erase [start lba] [end lba]";
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        try {
            ssdTestShell.eraserange(args[1], args[2]);
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length < 3;
    }
}
