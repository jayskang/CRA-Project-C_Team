package write;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WriteModuleTest {

    @Test
    void 기본_생성자_함수() {
        WriteModule writeModule = new WriteModule();
        assertNotNull(writeModule);
    }
}