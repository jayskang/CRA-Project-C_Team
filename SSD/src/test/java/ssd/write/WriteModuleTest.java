package ssd.write;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import read.ReadModule;
import write.WriteModule;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WriteModuleTest {

    @Spy
    private WriteModule writeModule;

    @Spy
    private ReadModule readModule;

    @BeforeEach
    void setUp() {
        this.writeModule = new WriteModule();
        this.readModule = new ReadModule();
    }

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
}