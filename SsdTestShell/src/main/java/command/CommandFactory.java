package command;

import scenario.TestScenarioFactory;
import shell.ISsdCommand;

import java.util.Hashtable;

import static constants.TestShellCommandConstants.*;

public class CommandFactory {
    private static final Hashtable<String, Command> commandTable = new Hashtable<>();

    public static Command getCommand(String commandString, ISsdCommand ssdTestShell) {
        switch (commandString) {
            case HELP:
                if(!commandTable.containsKey(HELP)) {
                    commandTable.put(HELP, new HelpCommand(ssdTestShell));
                }
                break;
            case EXIT:
                if(!commandTable.containsKey(EXIT)) {
                    commandTable.put(EXIT, new ExitCommand(ssdTestShell));
                }
                break;
            case WRITE:
                if(!commandTable.containsKey(WRITE)) {
                    commandTable.put(WRITE, new WriteCommand(ssdTestShell));
                }
                break;
            case READ:
                if(!commandTable.containsKey(READ)) {
                    commandTable.put(READ, new ReadCommand(ssdTestShell));
                }
                break;
            case FULLWRITE:
                if(!commandTable.containsKey(FULLWRITE)) {
                    commandTable.put(FULLWRITE, new FullWirteCommand(ssdTestShell));
                }
                break;
            case FULLREAD:
                if(!commandTable.containsKey(FULLREAD)) {
                    commandTable.put(FULLREAD, new FullReadCommand(ssdTestShell));
                }
                break;
            case TESTAPP_1:
                if(!commandTable.containsKey(TESTAPP_1)) {
                    commandTable.put(TESTAPP_1, new TestScriptCommand(TestScenarioFactory.getScenario(TESTAPP_1, ssdTestShell)));
                }
                break;
            case TESTAPP_2:
                if(!commandTable.containsKey(TESTAPP_2)) {
                    commandTable.put(TESTAPP_2, new TestScriptCommand(TestScenarioFactory.getScenario(TESTAPP_2, ssdTestShell)));
                }
                break;
            case ERASE:
                if(!commandTable.containsKey(ERASE)) {
                    commandTable.put(ERASE, new EraseCommand(ssdTestShell));
                }
                break;
            case ERASE_RANGE:
                if(!commandTable.containsKey(ERASE_RANGE)) {
                    commandTable.put(ERASE_RANGE, new EraseRangeCommand(ssdTestShell));
                }
                break;
        }

        return commandTable.get(commandString);
    }
}
