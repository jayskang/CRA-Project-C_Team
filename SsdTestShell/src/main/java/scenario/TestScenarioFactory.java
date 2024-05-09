package scenario;

import command.ISsdCommand;

import static constants.TestShellCommandConstants.*;

public class TestScenarioFactory {
    private static TestScenario testApp1;
    private static TestScenario testApp2;

    public static TestScenario getScenario(String scenarioName, ISsdCommand ssdTestShell) {
        TestScenario testScenario = null;
        switch (scenarioName) {
            case TESTAPP_1:
                if (testApp1 == null) {
                    testApp1 = new TestApp1(ssdTestShell);
                }
                testScenario = testApp1;
                break;
            case TESTAPP_2:
                if (testApp2 == null) {
                    testApp2 = new TestApp2(ssdTestShell);
                }
                testScenario = testApp2;
                break;
        }

        return testScenario;
    }
}
