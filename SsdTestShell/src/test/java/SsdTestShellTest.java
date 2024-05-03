import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        verify(mockSsd, times(1)).read("0");
    }
    @Test
    void read_함수_LAB_문자열_음수인_경우(){
        shell.read("-1");
        verify(mockSsd, times(1)).read("-1");
    }
    @Test
    void read_함수_LAB_문자열_99_초과인_경우(){
        shell.read("100");
        verify(mockSsd, times(1)).read("100");
    }
    @Test
    void read_함수_LAB_문자열_정수가_아닌_경우(){
        shell.read("A");
        verify(mockSsd, times(1)).read("A");
    }
    @Test
    void read_함수_LAB_문자열_null인_경우(){
        shell.read(null);
        verify(mockSsd, times(1)).read(null);
    }
    @Test
    void read_함수_driver_예외_catch_경우(){
//        spyShell.setSsd(any());
        doThrow(new IllegalArgumentException("INVALID Argument")).when(mockSsd).read("A");
        shell.read("A");
        verify(shell, times(1)).printError(any());
    }
}