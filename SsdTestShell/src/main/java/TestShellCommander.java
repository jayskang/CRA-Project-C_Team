import static java.lang.System.exit;

public class TestShellCommander {
    private static TestShellCommander testShellCommander;

    private Command command;
    private static String[] args;
    private ISsdTestShell ssdTestShell;

    private TestShellCommander(ISsdTestShell ssdTestShell) {
        this.ssdTestShell = ssdTestShell;
    }

    private static boolean isValidArgumentLength() {
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

    public static TestShellCommander getTestShellCommander(String[] args, ISsdTestShell ssdTestShell) {
        if(testShellCommander == null) {
            testShellCommander = new TestShellCommander(ssdTestShell);
        }

        TestShellCommander.args = args;
        if(!isValidArgumentLength()) {
            return null;
        }

        return testShellCommander;
    }

    public void runCommand() {
        command = CommandFactory.getCommand(args[0], ssdTestShell);
        command.setArgument(args);
        command.execute();
    }
}
