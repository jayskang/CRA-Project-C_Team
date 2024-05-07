package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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

        assertThat(getReadResult()).isEqualTo(VALID_VALUE);
    }

    @Test
    void 유효하지않은_주소값으로_데이터_쓰기() {
        // 정상적인 동작
        writeDataToAddressAndRead(0, VALID_VALUE);
        // 유효하지 않은 주소값으로 동작
        writeDataToAddressAndRead(-1, "0x11111111");

        assertThat(getReadResult()).isEqualTo(VALID_VALUE);
    }

    @Test
    void 유효하지않은_입력값으로_데이터_쓰기() {
        writeDataToAddressAndRead(0, VALID_VALUE);

        this.writeModule.write(0, "0x@%$&^#");
        this.readModule.read(0);

        assertThat(getReadResult()).isEqualTo(VALID_VALUE);
    }

    @Test
    void 유효하지않은_입력값으로_데이터_쓰기2() {
        writeDataToAddressAndRead(0, VALID_VALUE);
        writeDataToAddressAndRead(0, "FFFFFFFF");

        assertThat(getReadResult()).isEqualTo(VALID_VALUE);
    }

    @Test
    void 최대값_쓰기() {
        writeDataToAddressAndRead(0, "0xFFFFFFFF");
        assertThat(getReadResult()).isEqualTo("0xFFFFFFFF");
    }

    @Test
    void 데이터를_쓰고_nand파일을_지우고_조회하기() {
        writeDataToAddressAndRead(99, VALID_VALUE);

        if(deleteNand()) {
            this.readModule.read(99);
            assertThat(getReadResult()).isEqualTo(DEFAULT_VALUE);
        } else {
            fail();
        }
    }

    @Test
    void 데이터를_쓰고_nand파일을_지우고_다시_데이터쓰고_조회하기() {
        writeDataToAddressAndRead(99, VALID_VALUE);

        if(deleteNand()) {
            writeDataToAddressAndRead(99, VALID_VALUE);
            this.readModule.read(99);

            assertThat(getReadResult()).isEqualTo(VALID_VALUE);
        } else {
            fail();
        }
    }

    private boolean deleteNand() {
        File file = new File(FILE_ABSOLUTE_LOCATION + NAND_FILENAME);
        return file.delete();
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
