public class TestApp2Command extends TestScriptCommand {
    public TestApp2Command(ISsdCommand ssdTestShell) {
        super(ssdTestShell);
        ERROR_MESSAGE = "testapp2 command need no arguments. Please check Input.";
        HELP_MASSAGE = "Usage: testapp2";
    }

    @Override
    public void execute() {
        if(isInvalidArguments()) {
            printError();
            return;
        }

        scenario = new TestApp2(ssdTestShell);

        printResult(scenario.run());
    }
}
