import com.github.stefanbirkner.systemlambda.SystemLambda;
import shell.ISsdCommand;
import command.TestShellCommander;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.SSDExecutor;
import shell.SSDResultFileReader;
import shell.SsdTestShell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestShellCommanderTest {

    @Spy
    ISsdCommand ssdTestShell;

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
    @Test
    void TestShell_객체_생성() {
        assertThat(ssdTestShell).isNotNull();
    }

    @Test
    void 입력된_명령어가_없을때() {
        getCommander(new String[]{});

        String expected = "There is no command. Please Input command.Command." + System.lineSeparator();

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
    void write_호출() throws IOException {
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
    void read_호출() throws IOException {
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
    void fullwrite_호출() throws IOException {
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
    void fullread_호출() throws IOException {
        getCommander(new String[]{"fullread"});
        testShellCommander.runCommand();

        verify(ssdTestShell, times(1)).fullread();
    }

    @Test
    void testapp1_help_호출() {
        getCommander(new String[]{"help", "testapp1"});
        testShellCommander.runCommand();

        String expected = "Usage: testapp1" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void testapp1_매개변수_있을때() {
        getCommander(new String[]{"testapp1", "wrong"});
        testShellCommander.runCommand();

        String expected = "testapp1 command need no arguments. Please check Input." + System.lineSeparator();
        expected += "Usage: testapp1" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void testapp1_호출() {
        getRealCommander(new String[]{"testapp1"});
        testShellCommander.runCommand();

        String expected = "scenario.TestApp1...PASS!" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void testapp1_실패하도록_호출() {
        getCommander(new String[]{"testapp1"});
        testShellCommander.runCommand();

        String expected = "scenario.TestApp1...FAIL!" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void testapp2_help_호출() {
        getCommander(new String[]{"help", "testapp2"});
        testShellCommander.runCommand();

        String expected = "Usage: testapp2" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void testapp2_매개변수_있을때() {
        getCommander(new String[]{"testapp2", "wrong"});
        testShellCommander.runCommand();

        String expected = "testapp2 command need no arguments. Please check Input." + System.lineSeparator();
        expected += "Usage: testapp2" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void testapp2_호출() {
        getRealCommander(new String[]{"testapp2"});
        testShellCommander.runCommand();

        String expected = "scenario.TestApp2...PASS!" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void testapp2_실패하도록_호출() {
        getCommander(new String[]{"testapp2"});
        testShellCommander.runCommand();

        String expected = "scenario.TestApp2...FAIL!" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void 없는명령어_실행할_떄() {
        getCommander(new String[]{"wrong"});
        testShellCommander.runCommand();

        String expected = "INVALID COMMAND" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void erase_help_호출() {
        getCommander(new String[]{"help", "erase"});
        testShellCommander.runCommand();

        String expected = "Usage: erase [lba] [size]" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void erase_매개변수_없을때() {
        getCommander(new String[]{"erase"});
        testShellCommander.runCommand();

        String expected = "erase need lba and size." + System.lineSeparator();
        expected += "Usage: erase [lba] [size]" + System.lineSeparator();

        assertOutput(expected);
    }

    @Test
    void erase_실행() throws IOException {
        getCommander(new String[]{"erase", "0", "2"});
        testShellCommander.runCommand();

        verify(ssdTestShell, times(1)).erase("0", "2");
    }

    private void getCommander(String[] args) {
        testShellCommander = new TestShellCommander(args, ssdTestShell);
        if(!testShellCommander.isValidArgumentLength()) {
            testShellCommander = null;
        }
    }

    private void getRealCommander(String[] args) {
        SsdTestShell ssdTestShell = new SsdTestShell();
        SSDExecutor ssdExecutor = new SSDExecutor();
        ssdExecutor.setResultFileReader(new SSDResultFileReader());
        ssdTestShell.setSsd(ssdExecutor);
        testShellCommander = new TestShellCommander(args, ssdTestShell);
        if(!testShellCommander.isValidArgumentLength()) {
            testShellCommander = null;
        }
    }
}