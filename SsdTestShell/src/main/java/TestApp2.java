public class TestApp2 implements Scenario {
    private final SsdTestShell shell;
    private final int START_LBA = 0;
    private final int END_LBA = 5;
    boolean testResult = false;

    TestApp2(SsdTestShell ssdTestShell) {
        this.shell = ssdTestShell;
    }

    @Override
    public void testRun() {
        try {
            for (int i = 0; i < 30; i++)
                rangeWrite("0xAAAABBBB");
            rangeWrite("0x12345678");

            for (int i = START_LBA; i <= END_LBA; i++) {
                String readResult = shell.read(Integer.toString(i));
                if (!readResult.equals("0x12345678")) {
                    testResult = false;
                    return;
                }
            }
            testResult = true;
        } catch (Exception e) {
            testResult = false;
        }
    }

    private void rangeWrite(String data) throws IllegalArgumentException{
        for (int i = START_LBA; i <= END_LBA; i++) {
            shell.write(Integer.toString(i), data);
        }
    }

    @Override
    public boolean isPassed() {
        return testResult;
    }
}
