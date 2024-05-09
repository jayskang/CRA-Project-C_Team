import constants.Messages;

import static java.lang.System.exit;

public class TestShellCommander {
    private String[] args;
    private ISsdCommand ssdTestShell;

    public TestShellCommander(String[] args, ISsdCommand ssdTestShell) {
        this.args = args;
        this.ssdTestShell = ssdTestShell;
    }

    public boolean isValidArgumentLength() {
        if(args.length == 0) {
            System.out.println("There is no command. Please Input Command.");
            return false;
        }

        if(args.length > 3) {
            System.out.println("There is more than 3 argument. Please check input.");
            return false;
        }

        return true;
    }

    public void runCommand() {
        Command command = CommandFactory.getCommand(args[0], ssdTestShell);
        if(command == null) {
            System.out.println(Messages.ERROR_MSG_INVALID_COMMAND);
            return;
        }
        command.setArgument(args);
        command.execute();
    }
}
