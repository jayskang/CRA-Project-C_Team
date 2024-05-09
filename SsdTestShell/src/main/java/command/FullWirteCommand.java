package command;

import java.io.IOException;

public class FullWirteCommand extends AbstractCommand {
    public FullWirteCommand(ISsdCommand ssdTestShell) {
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

        try {
            ssdTestShell.fullwrite(args[1]);
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length < 2;
    }
}
