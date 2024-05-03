package command;

import jdk.jfr.Unsigned;
import read.ReadCore;
import write.WriteCore;

public class Commander {
    private ReadCore readCore;
    private WriteCore writeCore;

    private String command;
    private int lba = -1;
    private String inputData;

    public Commander(String[] args, ReadCore readCore, WriteCore writeCore) {
        if(args.length == 0 || args.length > 3) {
            return;
        }

        command = args[0];

        try {
            lba = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return;
        }

        if(command.equals("W")) {
            try {
                inputData = args[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                return;
            }
        }

        this.readCore = readCore;
        this.writeCore = writeCore;
    }

    public void runCommand() {
        if(isInvalidArgument()) {
            return;
        }

        switch (command) {
            case "R":
                readCore.read(lba);
                break;
            case "W":
                if(isInputDataIsNullOrEmpty()) {
                    return;
                }
                writeCore.write(lba, inputData);
                break;
            default:
                return;
        }
    }

    private boolean isInputDataIsNullOrEmpty() {
        return inputData == null || inputData.isEmpty();
    }

    private boolean isInvalidArgument() {
        return command == null || lba == -1;
    }
}
