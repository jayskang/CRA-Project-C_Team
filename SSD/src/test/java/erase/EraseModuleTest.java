package erase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.*;
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
        setUpStates(0, 10, "0x11111111");
        this.eraseModule.E(0, 10);

        testStateToEachLba(0, 10, DEFAULT_VALUE);
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

    private void testStateToEachLba(int startLba, int endLba, String expected) {
        for (int lba = startLba; lba < endLba; lba += 1) {
            this.readModule.read(lba);
            String actual = getReadResult();

            assertThat(actual).isEqualTo(expected);
        }
    }

    private void setUpStates(int startLba, int endLba, String value) {
        for(int lba = startLba; lba < endLba; lba += 1) {
            this.writeModule.write(lba, value);
        }
    }
}