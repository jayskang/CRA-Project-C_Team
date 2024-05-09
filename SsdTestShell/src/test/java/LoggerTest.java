import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoggerTest {

    @Test
    void loggingPrintTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        String methodName = "LoggerTest.loggingPrintTest()";


        String expected = LocalDateTime.now().format(DateTimeFormatter.ofPattern("'['yy/MM/dd HH:mm']'"))
                + " " + methodName + "\t" + "test\n";

        Logger log = Logger.makeLog();
        log.logging("test");
        String actual = outputStream.toString();

        assertEquals(expected.trim(), actual.trim());

    }
}
