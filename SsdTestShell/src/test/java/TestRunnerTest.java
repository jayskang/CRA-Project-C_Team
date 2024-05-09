import org.junit.jupiter.api.Test;

import java.io.IOException;

class TestRunnerTest {
    @Test
    void mock없이_runner_정상동작_테스트() throws IOException {
        // 테스트 전 ./run_list.lst 파일과 ./ssd.jar 파일이 필요합니다.

        SsdTestShell shell = new SsdTestShell();
        SSDExecutor ssd = new SSDExecutor();
        shell.setSsd(ssd);
        ssd.setResultFileReader(new SSDResultFileReader());

        TestRunner testRunner = new TestRunner(shell);
        testRunner.runScenariosFromFile("./run_list.lst");
    }
}