package ssd.write;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import read.ReadModule;
import read.SsdFileReader;
import write.WriteModule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteModuleTest {

    @Spy
    private WriteModule writeModule;

    @Spy
    private ReadModule readModule;

    @Spy
    private SsdFileReader ssdFileReader;


    @Test
    void 주소값이_0_미만일때() {
        int address = -1;
        String value = "0x00000001";

        this.writeModule.write(address, value);

        assertThat(this.writeModule.getErrorLog().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    void 주소값이_100_이상일때() {

        int address = 100;
        String value = "0x00000001";

        writeModule.write(address, value);

        assertThat(this.writeModule.getErrorLog().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    void 저장할_입력값이_포맷을_벗어났을때() {

        int address = 0;
        String value = "00000001";

        this.writeModule.write(address, value);

        assertThat(this.writeModule.getErrorLog().getClass()).isEqualTo(NumberFormatException.class);
    }

    @Test
    void 입력값에_이상한_값이_있을때() {

        int address = 0;
        String value = "!@#$%";

        this.writeModule.write(address, value);

        assertThat(this.writeModule.getErrorLog().getClass()).isEqualTo(NumberFormatException.class);
    }

    @Test
    void 값이_제대로_저장되었는지_테스트() {

        int address = 0;
        String value = "0x1234ABCD";

        String[] readFileContents = new String[]{"0x1234ABCD"};
        String expected = "0x1234ABCD";

        when(this.ssdFileReader.readFile()).thenReturn(readFileContents);

        this.writeModule.write(address, value);
        this.readModule.read(address);

        String[] readFileResult = this.ssdFileReader.readFile();

        assertThat(readFileResult[address]).isEqualTo(expected);
    }
}