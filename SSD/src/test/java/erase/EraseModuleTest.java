package erase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EraseModuleTest {

    private EraseModule eraseModule;

    @Test
    void 기본_생성자() {
        this.eraseModule = new EraseModule();

        assertNotNull(this.eraseModule);
    }
}