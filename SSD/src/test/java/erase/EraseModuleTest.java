package erase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.DEFAULT_VALUE;
import static cores.SSDConstraint.RESULT_FILENAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        this.eraseModule.erase(0, 10);

        testStateToEachLba(0, 10, DEFAULT_VALUE);
    }

    @Test
    void 허용되지않는_LBA까지_접근하려는_경우() {
        setUpStates(98, 100, ALL_1BIT_VALUE);
        // 접근 가능한 범위까지(99까지)만, erase 를 진행합니다.
        this.eraseModule.erase(98, 10);

        testStateToEachLba(98, 10, DEFAULT_VALUE);
    }

    @Test
    void 시작LBA가_음수_일경우() {
        setUpStates(0, 9, ALL_1BIT_VALUE);

        this.eraseModule.erase(-1, 9);

        testStateToEachLba(0, 9, ALL_1BIT_VALUE);
    }

    @Test
    void 사이즈가_10을_초과한경우() {
        setUpStates(0, 11, ALL_1BIT_VALUE);
        // size가 10을 초과하면, (또는 0일경우) EraseModule 에선 어떠한 동작도 하지 않습니다.
        this.eraseModule.erase(0, 11);

        testStateToEachLba(0, 11, ALL_1BIT_VALUE);
    }

    @Test
    void 유효한_사이즈_값이지만_LBA가_범위를_벗어난경우() {
        setUpStates(92, 99, ALL_1BIT_VALUE);
        // 유효한 LBA 값에서 시작했지만 범위를 벗어나면,
        // MAX_BOUNDARY(100)까지만 erase 합니다.
        this.eraseModule.erase(92, 10);

        testStateToEachLba(92, 99, DEFAULT_VALUE);
    }

    @Test
    void 전체범위_쓰고_지우기() {
        String[] values = new String[]{
                "0x11111111",
                "0x22222222",
                "0x33333333",
                "0x44444444",
                "0x55555555",
                "0x66666666",
                "0x77777777",
                "0xAAAAAAAA",
                "0xCCCCCCCC",
                "0xFFFFFFFF",
        };
        int[] startLba = new int[]{0, 10, 20, 30 ,40 ,50 ,60 ,70, 80, 90};

        for(int i = 0; i < 10; i += 1) {
            int lba = startLba[i];
            setUpStates(lba, lba + 10, values[i]);
            this.eraseModule.erase(lba + 1, 10);
        }

        for(int i = 0; i < 10; i += 1) {
            int lba = startLba[i];
            String expected = values[i];

            this.readModule.read(lba);
            String actual = getReadResult();

            assertThat(actual).isEqualTo(expected);
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

    private void testStateToEachLba(int startLba, int endLba, String expected) {
        for (int lba = startLba; lba < endLba; lba += 1) {
            this.readModule.read(lba);
            String actual = getReadResult();

            assertThat(actual).isEqualTo(expected);
        }
    }

    private void setUpStates(int startLba, int endLba, String value) {
        for (int lba = startLba; lba < endLba; lba += 1) {
            this.writeModule.write(lba, value);
        }
    }
}