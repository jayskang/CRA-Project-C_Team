package write;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WriteModuleTest {

    @Test
    void 기본_생성자_함수() {
        WriteModule writeModule = new WriteModule();
        assertNotNull(writeModule);
    }

    @Test
    void 정상적인_데이터_쓰기() {
        WriteModule writeModule = new WriteModule();

        int address = 0;
        String value = "0x00000001";

        writeModule.write(address, value);
    }
}