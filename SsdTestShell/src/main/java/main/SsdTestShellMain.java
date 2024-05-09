package main;

import command.TestShellCommander;
import runner.TestRunner;
import shell.SSDExecutor;
import shell.SSDResultFileReader;
import shell.SsdTestShell;

import java.io.IOException;
import java.util.Scanner;

public class SsdTestShellMain {
    public static void main(String[] args) {
        if(isTestRunnerScriptExist(args)) {
            TestRunner testRunner = new TestRunner(getSsdTestShell());
            try {
                testRunner.runScenariosFromFile(args[0]);
            } catch (IOException e) {
                System.out.println("Fail!!");
                System.out.println(e.getMessage());
            }
            return;
        }

        SsdTestShellMain.run();
    }

    private static boolean isTestRunnerScriptExist(String[] args) {
        return args.length != 0;
    }

    public static void run() {
        printInitMessage();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            String[] args = userInput.split(" ");
            TestShellCommander testShellCommander = new TestShellCommander(args, getSsdTestShell());
            testShellCommander.runCommand();
            System.out.print("> ");
        }
    }

    private static SsdTestShell getSsdTestShell() {
        SsdTestShell ssdCommand = new SsdTestShell();
        SSDExecutor ssdExecutor = new SSDExecutor();
        ssdExecutor.setResultFileReader(new SSDResultFileReader());
        ssdCommand.setSsd(ssdExecutor);
        return ssdCommand;
    }

    private static void printInitMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("SSD Test Shell Application").append(System.lineSeparator());
        builder.append("-------command.Command List-------").append(System.lineSeparator());
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
    }
}
