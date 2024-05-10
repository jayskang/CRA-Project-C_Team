package command;

public class Commander {
    String[] args;

    public Commander(String[] args) {
        this.args = args;
    }

    public void runCommand() {
        Command command = CommandFactory.getCommand(args);
        if(command == null) {
            return;
        }
        if(!command.isValidArgument()) {
            return;
        }

        command.executeCommand();
    }
}
