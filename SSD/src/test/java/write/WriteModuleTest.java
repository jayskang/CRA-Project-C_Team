package write;

import cores.ExceptionMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WriteModuleTest {

    @Mock
    private WriteModule WRITE_MODULE;

    @Test
    void 주소값이_0_미만일때() {
        WriteModule writeModule = this.WRITE_MODULE;

        int address = -1;
        String value = "0x00000001";

        try {
            writeModule.write(address, value);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException.getMessage()).isEqualTo(ExceptionMessage.ILLEGAL_ADDRESS_VALUE_EXCEPTION_MSG);
        }
        verify(writeModule, times(1)).write(address, value);
    }
}