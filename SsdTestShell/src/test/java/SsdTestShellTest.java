import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SsdTestShellTest {
    private static ByteArrayOutputStream outputMessage;

    @Mock
    SSD ssdMock;
    @Spy
    SsdTestShell ssdTestShell;

    @BeforeEach
    void setUp() {
        ssdTestShell.setSsd(ssdMock);
    }

    @Test
    void SsdTestShell_객체_정상적으로_생성() {
        assertNotNull(ssdTestShell);
    }

    @Test
    void testShell의_write가_호출되면_ssdMock의_write_호출() {
        ssdTestShell.write("3", "0x12345678");

        verify(ssdMock, times(1)).write("3", "0x12345678");
    }

    @Test
    void lba에_숫자가_아닌_값이_들어와도_write호출() {
        String[] nonNumericStrs = {"??", "0a", ""};
        for (String nonNumeric : nonNumericStrs) {
            ssdTestShell.write(nonNumeric, "0x12345678");
            verify(ssdMock, times(1)).write(nonNumeric, "0x12345678");
        }
    }

    @Test
    void data에_유효하지_않은_값이_들어와도_write호출() {
        String[] nonNumericStrs = {"??", "0a", ""};
        for (String nonNumeric : nonNumericStrs) {
            ssdTestShell.write("3", nonNumeric);
            verify(ssdMock, times(1)).write("3", nonNumeric);
        }
    }

    @Test
    void ssd_write에서_예외_발생시_예외처리() {
        doThrow(new IllegalArgumentException("error message")).when(ssdMock).write("XX", "0x12345678");

        ssdTestShell.write("XX", "0x12345678");

        verify(ssdTestShell, times(1)).printError(any());
    }
}