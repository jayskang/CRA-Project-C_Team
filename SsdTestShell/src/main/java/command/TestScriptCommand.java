package command;

import scenario.TestScenario;

public class TestScriptCommand extends AbstractCommand {
    TestScenario scenario;

    public TestScriptCommand(TestScenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        printResult(scenario.run());
    }

    protected void printResult(boolean testResult) {
        if (testResult) {
            System.out.println(scenario.getClass().getSimpleName() + "...PASS!");
            return;
        }
        System.out.println(scenario.getClass().getSimpleName() + "...FAIL!");
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length > 1;
    }
}
