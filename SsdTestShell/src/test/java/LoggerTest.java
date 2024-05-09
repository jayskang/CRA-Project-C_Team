import logger.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static constants.Logging.BYTES;
import static constants.Logging.LATEST_LOGFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoggerTest {
    Logger log;
    ByteArrayOutputStream outputStream;
    File currentLogFile;

    @BeforeEach
    void setUp() throws IOException {
        log = Logger.makeLog();
        outputStream = new ByteArrayOutputStream(); //outputStream.reset();
        System.setOut(new PrintStream(outputStream));
        currentLogFile = new File(LATEST_LOGFILE_NAME);
    }

    @Test
    void 로그_프린트_확인() throws IOException {
        String expected = getExpectedLoggingMessage("LoggerTest.로그_프린트_확인()");

        log.print("test");
        String actual = outputStream.toString();

        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    void 로그_파일쓰기_확인() throws IOException {
        int expected = 10;

        for (int logCnt = 0; logCnt < expected; logCnt++) {
            log.print("test #" + logCnt);
        }

        assertEquals(expected, getLogFileLineCount("로그_파일쓰기_확인"));
    }

    @Test
    void log객체_중복_생성_처리_확인() throws IOException {
        Logger firstLogger = Logger.makeLog();
        Logger seceondLogger = Logger.makeLog();
        int expected = 10;

        for (int logCnt = 0; logCnt < expected; logCnt++) {
            firstLogger.print("First logger.Logger #" + logCnt);
            seceondLogger.print("Second logger.Logger #" + logCnt);
        }

        assertEquals(expected * 2, getLogFileLineCount("log객체_중복_생성_처리_확인"));
    }

    @Test
    void 로그파일분리확인() throws IOException {

        for (int logCnt = 0; logCnt < 400; logCnt++) {
            log.print("test #" + logCnt);
        }

        assertThat(currentLogFile.length() / BYTES)
                .isLessThan(10);
    }

    private static String getExpectedLoggingMessage(String method) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("'['yy/MM/dd HH:mm']'"))
                + " " + method + "\t" + "test\n";
    }

    private static int getLogFileLineCount(String method) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(LATEST_LOGFILE_NAME));
        int count = 0;
        String log = reader.readLine();
        while (log != null) {
            if (log.contains(method)) {
                count++;
            }
            log = reader.readLine();
        }
        return count;
    }

}
