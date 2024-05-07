import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SsdTestShellTest {
    @Mock
    SSD mockSsd;
    @Mock
    SSDResultFileReader resultFileReader;
    @Spy
    SSD spySsd;
    @Spy
    private SsdTestShell shell;

    @BeforeEach
    void setUp() {
        shell.setSsd(mockSsd);
    }

    @Test
    void read_함수_LBA_문자열_정상인_경우() throws IOException {
        shell.read("1");
        verify(mockSsd, times(1)).read("1");
    }

    @Test
    void SsdTestShell_객체_정상적으로_생성() {
        assertNotNull(shell);
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
    void write_함수_testShell의_write가_호출되면_mockSsd의_write_호출() {
        shell.write("3", "0x12345678");

        verify(mockSsd, times(1)).write("3", "0x12345678");
    }

    @Test
    void write_함수_data가_0x로_시작하지_않으면_예외처리() {
        shell.write("3", "1234567800");

        verify(mockSsd, times(0)).write("3", "1234567800");
    }

    @Test
    void write_함수_data의_길이가_10이_아니면_예외처리() {
        shell.write("3", "0x1234567");

        verify(mockSsd, times(0)).write("3", "0x1234567");
    }

    @Test
    void write_함수_lba에_숫자가_아닌_값이_들어오면_예외처리() {
        String[] nonNumericStrs = {"not number", "include number 0", "", null};

        for (String nonNumeric : nonNumericStrs) {
            shell.write(nonNumeric, "0x12345678");
            verify(mockSsd, times(0)).write(nonNumeric, "0x12345678");
        }
    }

    @Test
    void write_함수_data에_숫자가_아닌_값이_들어오면_예외처리() {
        String[] nonNumericStrs = {"not number", "include number 0", "", null};

        for (String nonNumeric : nonNumericStrs) {
            shell.write("3", nonNumeric);
            verify(mockSsd, times(0)).write("3", nonNumeric);
        }
    }

    @Test
    void shell_Read_함수_파일사용_출력_결과() throws IOException {
        shell.setSsd(spySsd);
        doReturn("0 0x11111111").when(spySsd).readResultFile();

        shell.read("0");

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

    @Test
    void ssd_fullread_read함수_호출_횟수() throws IOException {
        shell.fullread();
        verify(shell, times(100)).read(anyString());
    }
}