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

    private final String ALL_1BIT_VALUE = "0x11111111";

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
        setUpStates(0, 10, ALL_1BIT_VALUE);
        this.eraseModule.E(0, 10);

        testStateToEachLba(0, 10, DEFAULT_VALUE);
    }

    @Test
    void 허용되지않는_LBA까지_접근하려는_경우() {
        setUpStates(98, 100, ALL_1BIT_VALUE);
        /**
         * 접근 가능한 범위까지(99까지)만,
         * erase 를 진행합니다.
         */
        this.eraseModule.E(98, 10);

        testStateToEachLba(98, 10, DEFAULT_VALUE);
    }

    @Test
    void 시작LBA가_음수_일경우() {
        setUpStates(0, 9, ALL_1BIT_VALUE);

        this.eraseModule.E(-1, 9);

        testStateToEachLba(0, 9, ALL_1BIT_VALUE);
    }

    @Test
    void 사이즈가_10을_초과한경우() {
        setUpStates(0, 11, ALL_1BIT_VALUE);
        /**
         * size가 10을 초과하면, (또는 0일경우)
         * EraseModule 에선 어떠한 동작도 하지 않습니다.
         */
        this.eraseModule.E(0, 11);

        testStateToEachLba(0, 11, ALL_1BIT_VALUE);
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