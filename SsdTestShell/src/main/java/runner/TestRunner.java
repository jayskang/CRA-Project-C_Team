package runner;

import shell.ISsdCommand;
import scenario.TestScenario;
import scenario.TestScenarioFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static constants.Messages.ERROR_MSG_RUN_LIST_FILE_NOT_FOUNDED;

public class TestRunner {
    ISsdCommand shell;

    public TestRunner(ISsdCommand shell) {
        this.shell = shell;
    }

    public void runScenariosFromFile(String runListFilePath) throws IOException {
        ArrayList<String> scenarioNames = loadScenariosFromFile(runListFilePath);
        for (String scenarioName : scenarioNames) {
            System.out.print(scenarioName + "\t---\tRun...");
            System.out.flush();
            if (runScenarioByName(scenarioName)) {
                System.out.println("Pass");
            } else {
                System.out.println("FAIL!");
                break;
            }
        }
    }

    public ArrayList<String> loadScenariosFromFile(String runListFilePath) throws IOException {
        try {
            ArrayList<String> result = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(runListFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String trimedLine = line.trim();
                if (!trimedLine.isEmpty()) {
                    result.add(trimedLine);
                }
            }
            reader.close();
            return result;
        }
        catch (IOException e) {
            throw new IOException(ERROR_MSG_RUN_LIST_FILE_NOT_FOUNDED);
        }
    }

    private boolean runScenarioByName(String scenarioName) {
        TestScenario testScenario = TestScenarioFactory.getScenario(scenarioName, shell);
        if (testScenario != null) {
            return testScenario.run();
        }
        else {
            return false;
        }
    }
}

