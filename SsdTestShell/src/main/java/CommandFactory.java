public class CommandFactory {
    private static HelpCommand helpCommand;
    private static ExitCommand exitCommand;

    public static Command getCommand(String commandString) {
        Command command = null;
        switch (commandString) {
            case "help":
                if(helpCommand == null) {
                    helpCommand = new HelpCommand();
                }
                command = helpCommand;

                break;
            case "exit":
                if(exitCommand == null) {
                    exitCommand = new ExitCommand();
                }
                command = exitCommand;
                break;
        }

        return command;
    }
}
