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
            logger.print(scenario.getClass().getSimpleName() + "...PASS!");
            return;
        }
        logger.print(scenario.getClass().getSimpleName() + "...FAIL!");
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length > 1;
    }
}
