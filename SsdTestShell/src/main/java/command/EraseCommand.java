package command;

import java.io.IOException;

public class EraseCommand extends AbstractCommand {
    public EraseCommand(ISsdCommand ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "erase need lba and size.";
        HELP_MASSAGE = "Usage: erase [lba] [size]";
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        try {
            ssdTestShell.erase(args[1], args[2]);
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length < 3;
    }
}
