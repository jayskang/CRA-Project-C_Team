import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestShellCommanderTest {

    @Spy
    ISsdTestShell ssdTestShell;

    private TestShellCommander testShellCommander;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
//        ssdTestShell = spy(ISsdTestShell.class);
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.out.println(getOutputStreamString());
    }
    @Test
    void TestShell_객체_생성() {
        assertThat(ssdTestShell).isNotNull();
    }

    @Test
    void 입력된_명령어가_없을때() {
        getCommander(new String[]{});

        String expected = "There is no command. Please Input Command." + System.lineSeparator();

        assertOutput(expected);
    }

    private String getOutputStreamString() {
        return outputStream.toString();
    }

    @Test
    void 입력된_값의_수가_3개_초과일때() {
        getCommander(new String[]{"write", "0", "0x00000001", "wrong"});

        String expected = "There is more than 3 argument. Please check input." + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void exit_명령어() throws Exception {
        getCommander(new String[]{"exit"});
        int statusCode = SystemLambda.catchSystemExit(testShellCommander::runCommand);

        assertThat(statusCode).isEqualTo(0);
    }

    @Test
    void exit_명령어에_다른_매개변수가_있을때() {
        getCommander(new String[]{"exit", "wrong"});
        testShellCommander.runCommand();

        String expected = "Exit command need no arguments. Please check Input." + System.lineSeparator();
        expected += "Usage: exit" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void help_명령어만_호출() {
        getCommander(new String[]{"help"});
        testShellCommander.runCommand();

        String expected = "Please input command to print help." + System.lineSeparator();
        expected += "Usage: help [command]" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void 없는_명령어의_help_호출() {
        getCommander(new String[]{"help", "wrong"});
        testShellCommander.runCommand();

        String expected = "Wrong command. Please check command." + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void exit_help_호출() {
        getCommander(new String[]{"help", "exit"});
        testShellCommander.runCommand();

        String expected = "Usage: exit" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void write_help_호출() {
        getCommander(new String[]{"help", "write"});
        testShellCommander.runCommand();

        String expected = "Usage: write [LBA] [data]" + System.lineSeparator();

        assertOutput(expected);
    }

    private void assertOutput(String expected) {
        assertThat(getOutputStreamString()).isEqualTo(expected);
    }

    @Test
    void read_help_호출() {
        getCommander(new String[]{"help", "read"});
        testShellCommander.runCommand();

        String expected = "Usage: read [LBA]" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void fullwrite_help_호출() {
        getCommander(new String[]{"help", "fullwrite"});
        testShellCommander.runCommand();

        String expected = "Usage: fullwrite [data]" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void fullread_help_호출() {
        getCommander(new String[]{"help", "fullread"});
        testShellCommander.runCommand();

        String expected = "Usage: fullread" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void write_매개변수_없을떄() {
        getCommander(new String[]{"write"});
        testShellCommander.runCommand();

        String expected = "Write need LBA and data." + System.lineSeparator();
        expected += "Usage: write [LBA] [data]" + System.lineSeparator();
    }

    @Test
    void write_호출() {
        getCommander(new String[]{"write", "0", "0x00000001"});
        testShellCommander.runCommand();

        verify(ssdTestShell, times(1)).write("0", "0x00000001");
    }

    @Test
    void read_매개변수_없을때() {
        getCommander(new String[]{"read"});
        testShellCommander.runCommand();

        String expected = "Write need LBA." + System.lineSeparator();
        expected += "Usage: read [LBA]" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void read_호출() {
        getCommander(new String[]{"read", "0"});
        testShellCommander.runCommand();

        verify(ssdTestShell, times(1)).read("0");
//        assertOutput("0x00000001");
    }

    @Test
    void fullwrite_매개변수_없을때() {
        getCommander(new String[]{"fullwrite"});
        testShellCommander.runCommand();

        String expected = "Fullwrite need data." + System.lineSeparator();
        expected += "Usage: fullwrite [data]" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void fullwrite_호출() {
        getCommander(new String[] {"fullwrite", "0x00000001"});
        testShellCommander.runCommand();

        verify(ssdTestShell, times(1)).fullwrite(anyString());
//        verify(ssdTestShell, times(100)).write(anyString(), "0x00000001");
    }

    @Test
    void fullread_매개변수_있을때() {
        getCommander(new String[]{"fullread", "wrong"});
        testShellCommander.runCommand();

        String expected = "" + System.lineSeparator();
        expected += "Usage: fullread" + System.lineSeparator();
    }

    @Test
    void fullread_호출() {
        getCommander(new String[]{"fullread"});
        testShellCommander.runCommand();

        verify(ssdTestShell, times(1)).fullread();
    }

    private void getCommander(String[] args) {
        testShellCommander = new TestShellCommander(args, ssdTestShell);
        if(!testShellCommander.isValidArgumentLength()) {
            testShellCommander = null;
        }
    }
}