import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SsdTestShellTest {
    @Mock
    SSD mockSsd;
    @Spy
    private SsdTestShell shell;

    @BeforeEach
    void setUp() {
        shell.setSsd(mockSsd);
    }

    @Test
    void read_함수_LBA_문자열_정상인_경우() throws IOException {
        shell.read("0");
        verify(mockSsd, times(1)).read("0");
    }

    @Test
    void read_함수_LBA_문자열_음수인_경우(){
        assertThatThrownBy(()->{
            shell.read("-1");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID COMMAND");
    }
    @Test
    void read_함수_LBA_문자열_99_초과인_경우(){
        assertThatThrownBy(()->{
            shell.read("100");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID COMMAND");
    }
    @Test
    void read_함수_LBA_문자열_정수가_아닌_경우(){
        assertThatThrownBy(()->{
            shell.read("A");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID COMMAND");
    }
    @Test
    void read_함수_LBA_문자열_null인_경우(){
        assertThatThrownBy(()->{
            shell.read(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID COMMAND");
    }

    @Test
    void SsdTestShell_객체_정상적으로_생성() {
        assertNotNull(shell);
    }

    @Test
    void testShell의_write가_호출되면_mockSsd의_write_호출() {
        shell.write("3", "0x12345678");

        verify(mockSsd, times(1)).write("3", "0x12345678");
    }

    @Test
    void lba에_숫자가_아닌_값이_들어와도_write호출() {
        String[] nonNumericStrs = {"??", "0a", ""};
        for (String nonNumeric : nonNumericStrs) {
            shell.write(nonNumeric, "0x12345678");
            verify(mockSsd, times(1)).write(nonNumeric, "0x12345678");
        }
    }

    @Test
    void data에_유효하지_않은_값이_들어와도_write호출() {
        String[] nonNumericStrs = {"??", "0a", ""};
        for (String nonNumeric : nonNumericStrs) {
            shell.write("3", nonNumeric);
            verify(mockSsd, times(1)).write("3", nonNumeric);
        }
    }

    @Test
    void ssd_write에서_예외_발생시_예외처리() {
        doThrow(new IllegalArgumentException("error message")).when(mockSsd).write("XX", "0x12345678");

        shell.write("XX", "0x12345678");

//        verify(shell, times(1)).printError(any());
    }

    @Spy
    private SsdTestShell shell_for_fileRead;
    @Spy
    SSD spySsd;
    @Mock
    SSDResultFileReader resultFileReader;

    @Test
    void shell_Read_함수_파일사용_출력_결과() throws IOException {
        shell_for_fileRead.setSsd(spySsd);
        doReturn("0 0x11111111").when(spySsd).readResultFile();
        shell_for_fileRead.read("0");
        verify(spySsd, times(1)).execSsdReadCommand("0");
        verify(spySsd, times(1)).readResultFile();
    }

    @Test
    void ssd_Read_함수_명령실행_및_파일읽기() throws IOException {
        // invalid argument일 경우 ssd의 read함수는 호출되지 않는다.
        doReturn("0 0x11111111").when(spySsd).readResultFile();
        spySsd.read("0");
        verify(spySsd, times(1)).execSsdReadCommand("0");
        verify(spySsd, times(1)).readResultFile();
    }

    @Test
    void ssd_readResultFile_함수() throws IOException {
        SSD ssd = new SSD();
        ssd.setResultFileReader(new SSDResultFileReader());
        System.out.println(ssd.readResultFile());
    }

    @Test
    void ssd_Read_함수_파일읽기_실패() throws IOException {
        spySsd.setResultFileReader(resultFileReader);
        doThrow(new IOException()).when(resultFileReader).readFile();
        assertThatThrownBy(()->{
            spySsd.readResultFile();
        }).isInstanceOf(IOException.class)
                .hasMessageContaining(SSD.ERROR_MSG_RESULT_FILE_NOT_FOUNDED);
    }
}