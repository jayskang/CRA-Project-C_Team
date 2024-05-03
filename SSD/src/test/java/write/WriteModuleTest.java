package write;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WriteModuleTest {

    @Spy
    private WriteModule WRITE_MODULE;

    @Test
    void 주소값이_0_미만일때() {
        WriteModule writeModule = this.WRITE_MODULE;

        int address = -1;
        String value = "0x00000001";

        writeModule.write(address, value);
        verify(writeModule, times(1)).write(address, value);
    }

    @Test
    void 주소값이_100_이상일때() {
        WriteModule writeModule = this.WRITE_MODULE;

        int address = 100;
        String value = "0x00000001";

        writeModule.write(address, value);
        verify(writeModule, times(1)).write(address, value);
    }

    @Test
    void 입력값이_포맷을_벗어났을때() {
        WriteModule writeModule = this.WRITE_MODULE;

        int address = 100;
        String value = "00000001";

        writeModule.write(address, value);
        verify(writeModule, times(1)).write(address, value);
    }


    @Test
    void 입력값에_이상한_값이_있을때() {
        WriteModule writeModule = this.WRITE_MODULE;

        int address = 0;
        String value = "!@#$%";

        writeModule.write(address, value);
        verify(writeModule, times(1)).write(address, value);
    }
}