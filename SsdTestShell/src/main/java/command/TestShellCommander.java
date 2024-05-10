package command;

import constants.Messages;
import logger.Logger;
import shell.ISsdCommand;

import java.io.IOException;

import static constants.Messages.*;
import static constants.Messages.ERR_LOGGER_INSTANCE_ALLOC_FAIL;

public class TestShellCommander {
    private String[] args;
    private ISsdCommand ssdTestShell;
    private Logger logger;
    public TestShellCommander(String[] args, ISsdCommand ssdTestShell) {
        this.args = args;
        this.ssdTestShell = ssdTestShell;
        try{
            logger = Logger.makeLog();
        } catch (IOException e){
            System.out.println(ERR_LOGGER_INSTANCE_ALLOC_FAIL);
        }
    }

    public boolean isValidArgumentLength() {
        if(args.length == 0) {
            logger.print("There is no command. Please Input command.Command.");
            return false;
        }

        if(args.length > 3) {
            System.out.println("There is more than 3 argument. Please check input.");
            return false;
        }

        return true;
    }

    public void runCommand() {
        Command command = CommandFactory.getCommand(args[0], ssdTestShell);
        if(command == null) {
            System.out.println(Messages.ERROR_MSG_INVALID_COMMAND);
            return;
        }
        command.setArgument(args);
        command.execute();
    }
}
