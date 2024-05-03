import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

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
    void read_함수_LAB_문자열_정상인_경우(){
        shell.read("0");
        verify(mockSsd, times(1)).read(0);
    }
    @Test
    void read_함수_LAB_문자열_음수인_경우(){
        shell.read("-1");
        verify(mockSsd, times(0)).read(-1);
        verify(shell, times(1)).printError(any());
    }
    @Test
    void read_함수_LAB_문자열_99_초과인_경우(){
        shell.read("100");
        verify(mockSsd, times(0)).read(100);
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

        verify(shell, times(1)).printError(any());
    }
}