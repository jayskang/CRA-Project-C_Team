public class TestScenarioFactory {
    private static TestScenario TestApp1;
    private static TestScenario TestApp2;

    public static TestScenario getScenario(String scenarioName, ISsdCommand ssdTestShell) {
        TestScenario testScenario = null;
        switch (scenarioName) {
            case "TestApp1":
                if (TestApp1 == null) {
                    TestApp1 = new TestApp1(ssdTestShell);
                }
                testScenario = TestApp1;
                break;
            case "TestApp2":
                if (TestApp2 == null) {
                    TestApp2 = new TestApp2(ssdTestShell);
                }
                testScenario = TestApp2;
                break;
        }

        return testScenario;
    }
}
