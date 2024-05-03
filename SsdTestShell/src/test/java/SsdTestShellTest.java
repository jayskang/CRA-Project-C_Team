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
    void read_success_lba(){
        shell.setSsd(ssd);
        shell.read("0");
        verify(ssd, times(1)).read(0);
    }
    @Test
    void read_fail_lba_negative_int(){
        shell.setSsd(ssd);
        shell.read("-1");
        verify(ssd, times(0)).read(-1);
    }
    @Test
    void read_fail_lba_over_99(){
        shell.setSsd(ssd);
        shell.read("100");
        verify(ssd, times(0)).read(100);
    }
    @Test
    void read_fail_lba_not_int(){
        shell.setSsd(ssd);
        shell.read("A");
        verify(ssd, times(0)).read(100);
    }
    @Test
    void read_fail_lba_not_int_null(){
        shell.setSsd(ssd);
        shell.read(null);
        verify(ssd, times(0)).read(100);
    }
}