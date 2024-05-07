package command;

import jdk.jfr.Unsigned;
import read.ReadCore;
import write.WriteCore;

public class Commander {
    public static final String WRITE = "W";
    public static final String READ = "R";

    private ReadCore readCore;
    private WriteCore writeCore;

    private String command;
    private int lba = -1;
    private String inputData;

    public Commander(String[] args, ReadCore readCore, WriteCore writeCore) {
        if(isInvalidArgsCount(args)) {
            return;
        }

        command = args[0];

        try {
            lba = Integer.parseInt(args[1]);

            if(command.equals(WRITE)) {
                inputData = args[2];
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return;
        }

        this.readCore = readCore;
        this.writeCore = writeCore;
    }

    private boolean isInvalidArgsCount(String[] args) {
        return args.length == 0 || args.length > 3;
    }

    public void runCommand() {
        if(isInvalidArgument()) {
            return;
        }

        switch (command) {
            case READ:
                readCore.read(lba);
                break;
            case WRITE:
                if(isInputDataIsNullOrEmpty()) {
                    return;
                }
                writeCore.write(lba, inputData);
                break;
            default:
                return;
        }
    }

    private boolean isInvalidArgument() {
        return command == null || lba == -1;
    }

    private boolean isInputDataIsNullOrEmpty() {
        return inputData == null || inputData.isEmpty();
    }
}