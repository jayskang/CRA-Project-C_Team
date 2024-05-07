package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.FILE_ABSOLUTE_LOCATION;
import static cores.SSDConstraint.RESULT_FILENAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ssdFullTest {

    private final String VALID_VALUE = "0x12341234";

    private WriteModule writeModule;
    private ReadModule readModule;

    @BeforeEach
    void setUp() {
        this.writeModule = new WriteModule();
        this.readModule = new ReadModule();
    }

    @Test
    void 기본_생성함수() {
        assertNotNull(this.writeModule);
        assertNotNull(this.readModule);
    }

    @Test
    void 정상적인_데이터_쓰기() {
        writeDataToAddressAndRead(0, VALID_VALUE);

        String actual = getReadResult();

        assertThat(actual).isEqualTo(VALID_VALUE);
    }

    @Test
    void 유효하지않은_주소값으로_데이터_쓰기() {
        // 정상적인 동작
        writeDataToAddressAndRead(0, VALID_VALUE);
        // 유효하지 않은 주소값으로 동작
        writeDataToAddressAndRead(-1, "0x11111111");

        String actual = getReadResult();

        assertThat(actual).isEqualTo(VALID_VALUE);
    }

    @Test
    void 유효하지않은_입력값으로_데이터_쓰기() {
        writeDataToAddressAndRead(0, VALID_VALUE);

        this.writeModule.write(0, "0x@%$&^#");
        this.readModule.read(0);

        String actual = getReadResult();

        assertThat(actual).isEqualTo(VALID_VALUE);
    }

    private void writeDataToAddressAndRead(int address, String inputValue) {
        this.writeModule.write(address, inputValue);
        this.readModule.read(address);
    }

    private String getReadResult() {
        String result = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME));
            result = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException ignored) {
        }
        return result;
    }
}
