package ssd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import write.WriteModule;

import static org.junit.jupiter.api.Assertions.*;

public class ssdFullTest {

    private WriteModule writeModule;

    @Test
    void 기본_생성함수() {
        this.writeModule = new WriteModule();
        assertNotNull(this.writeModule);
    }
}
