import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static constants.Messages.ERROR_MSG_RUN_LIST_FILE_NOT_FOUNDED;

public class TestRunner {
    private final SsdTestShell shell;
    private Map<String, TestScenario> scenarios;

    public TestRunner(SsdTestShell shell) {
        this.shell = shell;
        scenarios = new HashMap<>();
        initScenarios();
    }

    private void initScenarios() {
        scenarios.put("TestApp1", new TestApp1(shell));
        scenarios.put("TestApp2", new TestApp2(shell));
    }

    public void runScenariosFromFile(String runListFilePath) throws IOException {
        ArrayList<String> scenarioNames = loadScenariosFromFile(runListFilePath);
        for (String scenarioName : scenarioNames) {
            System.out.print(scenarioName + "\t---\tRun...");
            if (runScenario(scenarioName)) {
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

    private boolean runScenario(String scenarioName) {
        TestScenario testScenario = scenarios.get(scenarioName);
        if (testScenario != null) {
            return testScenario.run();
        }
        else {
            return false;
        }
    }
}


