import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoggerTest {

    @Test
    void loggingPrintTest() throws IOException {
        Logger log = Logger.makeLog();
        log.logging("test");
    }
}
