package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import read.ReadCore;
import read.ReadModule;
import write.WriteCore;
import write.WriteModule;

import java.io.*;

import static cores.SSDConstraint.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ssdFullTest {

    public static final String SAMPLE_VALUE = "0x1289CDEF";
    public static final int EXITS_VALUE_LUBA = 20;
    public static final int NULL_VALUE_LBA = 10;

    private WriteCore writeCore;
    private ReadCore readCore;

    private final String VALID_VALUE = "0x12341234";

    private WriteModule writeModule;
    private ReadModule readModule;


    @BeforeEach
    void setUp() {
        writeCore = new WriteModule();
        readCore = new ReadModule();

        this.writeModule = new WriteModule();
        this.readModule = new ReadModule();
    }

    @Test
    void 입력한_주소_Value_출력() throws IOException {
        writeCore.write(EXITS_VALUE_LUBA, SAMPLE_VALUE);
        readCore.read(EXITS_VALUE_LUBA);

        assertEquals(SAMPLE_VALUE, getReadResult());
    }

    @Test
    void 입력하지않은_주소_Value_출력() throws IOException {
        writeCore.write(EXITS_VALUE_LUBA, SAMPLE_VALUE);
        readCore.read(NULL_VALUE_LBA);

        assertEquals(DEFAULT_VALUE, getReadResult());

    }

    @Test
    void write하지않고_read() throws IOException {
        readCore.read(NULL_VALUE_LBA);

        assertEquals(DEFAULT_VALUE, getReadResult());

    }

    @Test
    void nand_파일_미존재() throws IOException {
        deleteNandFile();

        readCore.read(NULL_VALUE_LBA);

        assertEquals(DEFAULT_VALUE, getReadResult());

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

        if (deleteNand()) {
            this.readModule.read(99);
            assertThat(getReadResult()).isEqualTo(DEFAULT_VALUE);
        } else {
            fail();
        }
    }

    @Test
    void 데이터를_쓰고_nand파일을_지우고_다시_데이터쓰고_조회하기() {
        writeDataToAddressAndRead(99, VALID_VALUE);

        if (deleteNand()) {
            writeDataToAddressAndRead(99, VALID_VALUE);
            this.readModule.read(99);

            assertThat(getReadResult()).isEqualTo(VALID_VALUE);
        } else {
            fail();
        }
    }

    @Test
    void 같은곳에_5번_데이터쓰고_마지막_상태_읽어오기() {
        String[] values = new String[]{"0x12345678", "0xFFFACD21", "0xFFFFFFFF", "0x11111111", "0xCCCCCCCC"};

        for (String value : values) {
            writeDataToAddressAndRead(50, value);
        }
        assertThat(getReadResult()).isEqualTo("0xCCCCCCCC");
    }

    @Test
    void 값이_16진수를_벗어났을경우() {
        writeDataToAddressAndRead(0, "0x00000000");
        writeDataToAddressAndRead(0, "0x0000000G");
        assertThat(getReadResult()).isNotEqualTo("0x0000000G");
    }

    private static String getReadResult() {
        try {
            return new BufferedReader(new FileReader(
                    (RESULT_FILENAME)))
                    .readLine();
        } catch (IOException e) {
        }
        return "";
    }

    private static void deleteNandFile() {
        File file = new File(NAND_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    private void writeDataToAddressAndRead(int address, String inputValue) {
        this.writeModule.write(address, inputValue);
        this.readModule.read(address);
    }

    private boolean deleteNand() {
        File file = new File(NAND_FILENAME);
        return file.delete();
    }
}
