public abstract class TestScriptCommand extends AbstractCommand {
    Scenario scenario;

    public TestScriptCommand(ISsdCommand ssdTestShell) {
        super(ssdTestShell);
    }

    protected void printResult(boolean testResult) {
        if (testResult) {
            System.out.println(scenario.getClass().getName() + "...PASS!");
            return;
        }
        System.out.println(scenario.getClass().getName() + "...FAIL!");
    }

    @Override
    public boolean isInvalidArguments() {
        return args.length > 1;
    }
}
