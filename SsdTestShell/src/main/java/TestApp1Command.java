public class TestApp1Command extends TestScriptCommand {

    public TestApp1Command(ISsdCommand ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "testapp1 command need no arguments. Please check Input.";
        HELP_MASSAGE = "Usage: testapp1";
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        scenario = new TestApp1(ssdTestShell);
        scenario.testRun();

        printResult(scenario.isPassed());
    }
}
