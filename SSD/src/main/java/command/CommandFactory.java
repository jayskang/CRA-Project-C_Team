package command;

import static command.CommandConstant.*;

public class CommandFactory {
    public static Command getCommand(String[] args) {
        Command command = null;

        String commandString;
        try {
            commandString = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

        switch (commandString) {
            case READ:
                command = new ReadCommand(args);
                break;
            case WRITE:
                command = new WriteCommand(args);
                break;
            case ERASE:
                command = new EraseCommand(args);
                break;
        }

        return command;
    }
}
