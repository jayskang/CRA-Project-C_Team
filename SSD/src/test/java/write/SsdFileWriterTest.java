package write;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SsdFileWriterTest {

    private SsdFileWriter writer;

    @Test
    void 기본_생성자_함수() {
        this.writer = new SsdFileWriter();
    }

    @Test
    void 정상적인_저장() {
        this.writer = new SsdFileWriter();
        this.writer.store(0, "0x12341234");
    }
}