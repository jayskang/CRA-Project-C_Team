import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

class SsdTestShellTest {
    @Mock
    SSD ssd;
    private SsdTestShell shell;

    @BeforeEach
    void setUp() {
        shell = new SsdTestShell();
    }

    @Test
    void read_함수_LAB_문자열_정상인_경우(){
        shell.setSsd(ssd);
        shell.read("0");
        verify(ssd, times(1)).read(0);
    }
    @Test
    void read_함수_LAB_문자열_음수인_경우(){
        shell.setSsd(ssd);
        shell.read("-1");
        verify(ssd, times(0)).read(-1);
    }
    @Test
    void read_함수_LAB_문자열_99_초과인_경우(){
        shell.setSsd(ssd);
        shell.read("100");
        verify(ssd, times(0)).read(100);
    }
    @Test
    void read_함수_LAB_문자열_정수가_아닌_경우(){
        shell.setSsd(ssd);
        shell.read("A");
        verify(ssd, times(0)).read(100);
    }
    @Test
    void read_함수_LAB_문자열_null인_경우(){
        shell.setSsd(ssd);
        shell.read(null);
        verify(ssd, times(0)).read(100);
    }
}