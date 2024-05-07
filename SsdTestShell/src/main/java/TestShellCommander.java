import static java.lang.System.exit;

public class TestShellCommander {

    private final String[] args;

    public TestShellCommander(String[] args) {
        if(args.length == 0) {
            System.out.println("There is no command. Please Input Command.");
        }

        if(args.length > 3) {
            System.out.println("There is more than 3 argument. Please check input.");
        }

        this.args = args;
    }

    public void runCommand() {
        String command = args[0];
        if("exit".equals(command)) {
            exit(0);
        }
        if("help".equals(command)) {
            if(args.length == 1) {
                System.out.println("Please input command to print help.");
                System.out.println("Usage: help [command]");
            }
        }
    }
}
