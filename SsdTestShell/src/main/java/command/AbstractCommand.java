package command;

import logger.Logger;
import shell.ISsdCommand;

import java.io.IOException;

import static constants.Messages.ERR_LOGGER_INSTANCE_ALLOC_FAIL;

public abstract class AbstractCommand implements Command {

    Logger logger;
    protected String ERROR_MESSAGE = "";
    protected String HELP_MASSAGE = "";
    protected String[] args;
    protected ISsdCommand ssdTestShell;

    public AbstractCommand() {
        try{
            logger = Logger.makeLog();
        } catch (IOException e){
            System.out.println(ERR_LOGGER_INSTANCE_ALLOC_FAIL);
        }
    }

    public AbstractCommand(ISsdCommand ssdTestShell) {
        this();
        this.ssdTestShell = ssdTestShell;
    }

    @Override
    public void setArgument(String[] args) {
        this.args = args;
    }

    @Override
    public abstract void execute();

    @Override
    public void printError() {
        logger.print(ERROR_MESSAGE);
        printHelpMessage();
    }

    @Override
    public void printHelpMessage() {
        logger.print(HELP_MASSAGE);
    }

    @Override
    public abstract boolean isInvalidArguments();
}
