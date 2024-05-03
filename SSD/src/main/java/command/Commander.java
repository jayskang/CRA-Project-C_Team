package command;

import jdk.jfr.Unsigned;

public class Commander {
    private String command;
    private String lba;
    private String inputData;

    public Commander(String[] args) {
        if(args.length == 0 || args.length > 3) {
            throw new IllegalArgumentException();
        }

        command = args[0];
        lba = args[1];
        if(command.equals("W")) {
            try {
                inputData = args[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void runCommand() {
        switch (command) {
            case "R":
                break;
            case "W":
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
