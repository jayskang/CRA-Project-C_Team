package erase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.RESULT_FILENAME;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class EraseModuleTest {

    private EraseModule eraseModule;

    private WriteModule writeModule;
    private ReadModule readModule;

    @BeforeEach
    void setUp() {
        this.eraseModule = new EraseModule();
        this.writeModule = new WriteModule();
        this.readModule = new ReadModule();
    }

    @Test
    void 기본_생성자() {
        this.eraseModule = new EraseModule();

        assertNotNull(this.eraseModule);
    }

    @Test
    void 정상동작_테스트() {
        this.writeModule.write(0, "0x11111111");
        this.writeModule.write(1, "0x11111111");
        this.writeModule.write(2, "0x11111111");
        this.writeModule.write(3, "0x11111111");
        this.writeModule.write(4, "0x11111111");
        this.writeModule.write(5, "0x11111111");
        this.writeModule.write(6, "0x11111111");
        this.writeModule.write(7, "0x11111111");
        this.writeModule.write(8, "0x11111111");
        this.writeModule.write(9, "0x11111111");

        this.eraseModule.E(0, 10);

        for (int i = 0; i < 10; i += 1) {

            this.readModule.read(i);
            String actual = getReadResult();

            assertThat(actual).isEqualTo("0x00000000");
        }
    }

    private String getReadResult() {
        try {
            return new BufferedReader(new FileReader(
                    (RESULT_FILENAME)))
                    .readLine();
        } catch (IOException ignored) {
        }
        return null;
    }
}