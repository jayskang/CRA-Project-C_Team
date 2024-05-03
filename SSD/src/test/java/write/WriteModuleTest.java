package write;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WriteModuleTest {

    private WriteModule writeModule;

    @BeforeEach
    void setUp() {
        this.writeModule = new WriteModule();
    }

    @Test
    void 기본_생성자_함수() {
        assertNotNull(this.writeModule);
    }

    @Test
    void 정상적인_데이터_쓰기() {
        int address = 0;
        String value = "0x00000001";

        this.writeModule.write(address, value);
    }
}