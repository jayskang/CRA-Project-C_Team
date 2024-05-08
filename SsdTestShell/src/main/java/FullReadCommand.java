import java.io.IOException;

public class FullReadCommand extends AbstractCommand {
    public FullReadCommand(ISsdTestShell ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "Exit command need no arguments. Please check Input.";
        HELP_MASSAGE = "Usage: fullread";
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        try {
            ssdTestShell.fullread();
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length > 1;
    }
}
