package scenario;

import command.ISsdCommand;

import java.io.IOException;

public class TestApp2 implements TestScenario {
    public static final int LOOP_COUNT = 30;
    public static final String OLD_DATA = "0xAAAABBBB";
    public static final String NEW_DATA = "0x12345678";
    private final int START_LBA = 0;
    private final int END_LBA = 5;

    private final ISsdCommand shell;

    TestApp2(ISsdCommand ssdTestShell) {
        this.shell = ssdTestShell;
    }

    @Override
    public boolean run() {
        try {
            for (int i = 0; i < LOOP_COUNT; i++)
                rangeWrite(OLD_DATA);
            rangeWrite(NEW_DATA);

            for (int i = START_LBA; i <= END_LBA; i++) {
                String readResult = shell.read(Integer.toString(i));
                if (!readResult.equals(NEW_DATA)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void rangeWrite(String data) throws IllegalArgumentException, IOException {
        for (int i = START_LBA; i <= END_LBA; i++) {
            shell.write(Integer.toString(i), data);
        }
    }
}