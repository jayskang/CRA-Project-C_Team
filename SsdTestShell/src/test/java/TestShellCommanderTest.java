import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestShellCommanderTest {
    @Spy
    ISsdTestShell ssdTestShell;
    private TestShellCommander testShellCommander;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void TestShell_객체_생성() {
        assertThat(ssdTestShell).isNotNull();
    }

    @Test
    void 입력된_명령어가_없을때() {
        getCommander(new String[]{});

        String actual = outputStream.toString();
        String expected = "There is no command. Please Input Command." + System.lineSeparator();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 입력된_값의_수가_3개_초과일때() {
        getCommander(new String[]{"write", "0", "0x00000001", "wrong"});

        String actual = outputStream.toString();
        String expected = "There is more than 3 argument. Please check input." + System.lineSeparator();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void exit_명령어() throws Exception {
        getCommander(new String[]{"exit"});
        int statusCode = SystemLambda.catchSystemExit(testShellCommander::runCommand);

        assertThat(statusCode).isEqualTo(0);
    }

    @Test
    void help_명령어만_호출() {
        getCommander(new String[]{"help"});
        testShellCommander.runCommand();

        String actual =  outputStream.toString();
        String expected = "Please input command to print help." + System.lineSeparator();
        expected += "Usage: help [command]" + System.lineSeparator();

        assertThat(actual).isEqualTo(expected);
    }

    private void getCommander(String[] args) {
        testShellCommander = new TestShellCommander(args);
    }
}