package command.buffer;

import command.Buffer;
import command.Commander;
import erase.EraseModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BufferTest {

    private Buffer buffer;

    @BeforeEach
    void setUp() {
        this.buffer = Buffer.getInstance();
    }

    @Test
    void 기본_생성자() {
        assertNotNull(this.buffer);
    }

    @Test
    void write_명령어를_10개_입력() {
        this.buffer.push(
                new Commander(new String[]{"W", "0", "0x0000000F"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "1", "0x00000001"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "2", "0x00000002"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "3", "0x00000003"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "4", "0x00000004"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "5", "0x00000005"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "6", "0x00000006"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "7", "0x00000007"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "8", "0x00000008"},
                        null, new WriteModule(), null)
        );
        this.buffer.push(
                new Commander(new String[]{"W", "9", "0x00000009"},
                        null, new WriteModule(), null)
        );
        int actual = this.buffer.getCommanders().size();

        assertThat(actual).isEqualTo(10);
    }
}