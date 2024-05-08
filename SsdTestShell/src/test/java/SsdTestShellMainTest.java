import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SsdTestShellMainTest {
    private TestShellCommander testShellCommander;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.out.println(getOutputStreamString());
    }

    private String getOutputStreamString() {
        return outputStream.toString();
    }

    @Test
    void 명령어_입력콘솔_출력() throws Exception {
        executeCommand("exit");

        assertThat(getOutputStreamString()).isEqualTo(initOutput());
    }

    @Test
    void write_read_실행() throws Exception {
        String command = "write 0 0x12345678" + System.lineSeparator()
                + "read 0" + System.lineSeparator()
                + "exit";
        executeCommand(command);

        String expected = initOutput();
        expected += getInputSymbol();
        expected += "0x12345678" + System.lineSeparator();
        expected += getInputSymbol();

        assertThat(getOutputStreamString()).isEqualTo(expected);
    }

    @Test
    void fullwrite_read_실행() throws Exception {
        String command = "fullwrite 0x12345678" + System.lineSeparator()
                + "fullread" + System.lineSeparator()
                + "exit";

        executeCommand(command);

        String expected = initOutput();
        expected += getInputSymbol();
        for(int i = 0; i < 100; i++) {
            expected += i + " 0x12345678" + System.lineSeparator();
        }
        expected += getInputSymbol();

        assertThat(getOutputStreamString()).isEqualTo(expected);
    }

    private void executeCommand(String command) throws Exception {
        catchSystemExit(() -> {
            withTextFromSystemIn(command).execute(SsdTestShellMain::run) ;
        });
    }

    private String initOutput() {
        StringBuilder builder = new StringBuilder();
        builder.append("SSD Test Shell Application").append(System.lineSeparator());
        builder.append("-------Command List-------").append(System.lineSeparator());
        builder.append("write").append(System.lineSeparator());
        builder.append("read").append(System.lineSeparator());
        builder.append("fullwrite").append(System.lineSeparator());
        builder.append("fullread").append(System.lineSeparator());
        builder.append("help").append(System.lineSeparator());
        builder.append("exit").append(System.lineSeparator());
        builder.append("-----Test Script List-----").append(System.lineSeparator());
        builder.append("testapp1").append(System.lineSeparator());
        builder.append("testapp2").append(System.lineSeparator());
        builder.append("--------------------------").append(System.lineSeparator());
        builder.append(getInputSymbol());
        return builder.toString();
    }

    private String getInputSymbol() {
        return "> ";
    }
}