public class CommandFactory {
    private static HelpCommand helpCommand;
    private static ExitCommand exitCommand;
    private static WriteCommand writeCommand;
    private static ReadCommand readCommand;
    private static FullWirteCommand fullWirteCommand;
    private static FullReadCommand fullReadCommand;
    private static TestApp1Command testApp1Command;
    private static TestApp2Command testApp2Command;

    public static Command getCommand(String commandString, ISsdCommand ssdTestShell) {
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
            case "fullwrite":
                if(fullWirteCommand == null) {
                    fullWirteCommand = new FullWirteCommand(ssdTestShell);
                }
                command = fullWirteCommand;
                break;
            case "fullread":
                if(fullReadCommand == null) {
                    fullReadCommand = new FullReadCommand(ssdTestShell);
                }
                command = fullReadCommand;
                break;
            case "testapp1":
                if(testApp1Command == null) {
                    testApp1Command = new TestApp1Command(ssdTestShell);
                }
                command = testApp1Command;
                break;
            case "testapp2":
                if(testApp2Command == null) {
                    testApp2Command = new TestApp2Command(ssdTestShell);
                }
                command = testApp2Command;
                break;
        }

        return command;
    }
}
