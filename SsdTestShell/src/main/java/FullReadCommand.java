import java.io.IOException;
import java.util.ArrayList;

public class FullReadCommand extends AbstractCommand {
    public FullReadCommand(ISsdCommand ssdTestShell) {
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
            ArrayList<String> resultList = ssdTestShell.fullread();
            for(int i = 0; i < resultList.size(); i++) {
                System.out.println(i + " " + resultList.get(i));
            }
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length > 1;
    }
}
