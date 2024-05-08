package command.buffer;

import command.Buffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BufferTest {

    private Buffer buffer;

    @Test
    void 기본_생성자() {
        this.buffer = Buffer.getInstance();

        assertNotNull(this.buffer);
    }
}