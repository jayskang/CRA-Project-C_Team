public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        ERROR_MESSAGE = "Please input command to print help.";
        HELP_MASSAGE = "Usage: help [command]";
    }

    @Override
    public void execute() {
        if (isInvalidArguments()) {
            printError();
            return;
        }

        String commandString = args[1];
        Command command = CommandFactory.getCommand(commandString);
        if(command == null) {
            System.out.println("Wrong command. Please check command.");
            return;
        }
        command.printHelpMessage();
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length == 1;
    }
}
