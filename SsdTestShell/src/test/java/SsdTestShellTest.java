import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void SsdTestShell_객체_정상적으로_생성() {
        assertNotNull(shell);
    }

    @Test
    void read_함수_LAB_문자열_정상인_경우(){
        shell.read("0");
        verify(mockSsd, times(1)).read("0");
    }

    @Test
    void read_함수_LAB_문자열_음수인_경우(){
        shell.read("-1");
        verify(mockSsd, times(0)).read("-1");
        verify(shell, times(1)).printError(any());
    }

    @Test
    void read_함수_LAB_문자열_99_초과인_경우(){
        shell.read("100");
        verify(mockSsd, times(0)).read("100");
        verify(shell, times(1)).printError(any());
    }

    @Test
    void read_함수_LAB_문자열_정수가_아닌_경우(){
        shell.read("A");
        verify(mockSsd, times(0)).read(any());
        verify(shell, times(1)).printError(any());
    }

    @Test
    void read_함수_LAB_문자열_null인_경우(){
        shell.read(null);
        verify(mockSsd, times(0)).read(null);
        verify(shell, times(1)).printError(any());
    }

    @Test
    void write_함수_testShell의_write가_호출되면_mockSsd의_write_호출() {
        shell.write("3", "0x12345678");

        verify(mockSsd, times(1)).write("3", "0x12345678");
    }

    @Test
    void write_함수_data가_0x로_시작하지_않으면_예외처리() {
        assertThrows(IllegalArgumentException.class, () -> {
            shell.write("3", "1234567800");
        });
    }

    @Test
    void write_함수_data의_길이가_10이_아니면_예외처리() {
        assertThrows(IllegalArgumentException.class, () -> {
            shell.write("3", "0x1234567");
        });
    }

    @Test
    void write_함수_lba에_숫자가_아닌_값이_들어오면_예외처리() {
        String[] nonNumericStrs = {"not number", "include number 0", "", null};

        for (String nonNumeric : nonNumericStrs) {
            assertThrows(IllegalArgumentException.class, () -> {
                shell.write(nonNumeric, "0x12345678");
            });
        }
    }

    @Test
    void write_함수_data에_숫자가_아닌_값이_들어오면_예외처리() {
        String[] nonNumericStrs = {"not number", "include number 0", "", null};

        for (String nonNumeric : nonNumericStrs) {
            assertThrows(IllegalArgumentException.class, () -> {
                shell.write("3", nonNumeric);
            });
        }
    }

    @Test
    void fullWrite_함수_정상_호출시_write함수_100번_호출() {
        shell.fullwrite("0x12345678");

        verify(mockSsd, times(100)).write(anyString(), eq("0x12345678"));
    }

    @Test
    void fullWrite_함수_data값이_유효하지_않다면_write함수_0번_호출() {
        assertThrows(IllegalArgumentException.class, () -> {
            shell.fullwrite("0x1234");
        });
        verify(mockSsd, times(0)).write(anyString(), eq("0x1234"));
    }
}