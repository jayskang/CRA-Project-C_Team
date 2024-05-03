package command;

import jdk.jfr.Unsigned;
import read.ReadCore;
import write.WriteCore;

public class Commander {
    private ReadCore readCore;
    private WriteCore writeCore;

    private String command;
    private int lba;
    private String inputData;

    public Commander(String[] args, ReadCore readCore, WriteCore writeCore) {
        if(args.length == 0 || args.length > 3) {
            return;
        }

        command = args[0];
        lba = Integer.parseInt(args[1]);
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
        switch (command) {
            case "R":
                readCore.read(lba);
                break;
            case "W":
                writeCore.write(lba, inputData);
                break;
            default:
                return;
        }
    }
}
