package command;

import erase.EraseCore;
import read.ReadCore;
import write.WriteCore;

import static command.CommandConstant.*;

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
