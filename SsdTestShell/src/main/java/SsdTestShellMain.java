import java.util.Scanner;

public class SsdTestShellMain {
    public static void main(String[] args) {
        SsdTestShellMain.run();
    }

    public static void run() {
        StringBuilder builder = new StringBuilder();
        builder.append("SSD Test Shell Application").append(System.lineSeparator());
        builder.append("-------Command List-------").append(System.lineSeparator());
        builder.append("write").append(System.lineSeparator());
        builder.append("read").append(System.lineSeparator());
        builder.append("fullwrite").append(System.lineSeparator());
        builder.append("fullread").append(System.lineSeparator());
        builder.append("erase").append(System.lineSeparator());
        builder.append("erase_range").append(System.lineSeparator());
        builder.append("help").append(System.lineSeparator());
        builder.append("exit").append(System.lineSeparator());
        builder.append("-----Test Script List-----").append(System.lineSeparator());
        builder.append("testapp1").append(System.lineSeparator());
        builder.append("testapp2").append(System.lineSeparator());
        builder.append("--------------------------").append(System.lineSeparator());
        builder.append("> ");

        System.out.print(builder.toString());

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            String[] args = userInput.split(" ");
            SsdTestShell ssdCommand = new SsdTestShell();
            SSDExecutor ssdExecutor = new SSDExecutor();
            ssdExecutor.setResultFileReader(new SSDResultFileReader());
            ssdCommand.setSsd(ssdExecutor);
            TestShellCommander testShellCommander = new TestShellCommander(args, ssdCommand);
            testShellCommander.runCommand();
            System.out.print("> ");
        }
    }
}
