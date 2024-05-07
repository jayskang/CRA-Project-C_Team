public class CommandFactory {
    private static HelpCommand helpCommand;
    private static ExitCommand exitCommand;
    private static WriteCommand writeCommand;
    private static ReadCommand readCommand;

    public static Command getCommand(String commandString, ISsdTestShell ssdTestShell) {
        Command command = null;
        switch (commandString) {
            case "help":
                if(helpCommand == null) {
                    helpCommand = new HelpCommand(ssdTestShell);
                }
                command = helpCommand;

                break;
            case "exit":
                if(exitCommand == null) {
                    exitCommand = new ExitCommand(ssdTestShell);
                }
                command = exitCommand;
                break;
            case "write":
                if(writeCommand == null) {
                    writeCommand = new WriteCommand(ssdTestShell);
                }
                command = writeCommand;
                break;
            case "read":
                if(readCommand == null) {
                    readCommand = new ReadCommand(ssdTestShell);
                }
                command = readCommand;
                break;
        }

        return command;
    }
}
