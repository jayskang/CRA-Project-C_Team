import static java.lang.System.exit;

public class TestShellCommander {
    private String[] args;
    private ISsdTestShell ssdTestShell;

    public TestShellCommander(String[] args, ISsdTestShell ssdTestShell) {
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
        command.setArgument(args);
        command.execute();
    }
}
