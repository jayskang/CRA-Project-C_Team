import java.util.ArrayList;

public class TestApp1 implements TestScenario {
    public final String DATA = "0x12345678";

    private final ISsdCommand shell;

    TestApp1(ISsdCommand ssdTestShell) {
        this.shell = ssdTestShell;
    }

    @Override
    public boolean run() {
        try {
            shell.fullwrite(DATA);

            ArrayList<String> readResult = shell.fullread();
            for (String data : readResult) {
                if (!data.equals(DATA)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}