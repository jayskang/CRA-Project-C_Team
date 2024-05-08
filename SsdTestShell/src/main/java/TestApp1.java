import java.util.ArrayList;

public class TestApp1 implements Scenario {
    public final String DATA = "0x12345678";

    private final ISsdCommand shell;
    boolean testResult = false;

    TestApp1(ISsdCommand ssdTestShell) {
        this.shell = ssdTestShell;
    }

    @Override
    public void testRun() {
        try {
            shell.fullwrite(DATA);

            ArrayList<String> readResult = shell.fullread();
            if(readResult.isEmpty()) {
                testResult = false;
                return;
            }
            for (String data : readResult) {
                if (!data.equals(DATA)) {
                    testResult = false;
                    return;
                }
            }
            testResult = true;
        } catch (Exception e) {
            testResult = false;
        }
    }

    @Override
    public boolean isPassed() {
        return testResult;
    }
}
