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
    void NEW_E_OLD_W() {
        Commander erase = new Commander(new String[]{"E", "0", "1"},
                null, null, new EraseModule());

        this.buffer.push(
                new Commander(new String[]{"W", "0", "0x00000001"},
                null, new WriteModule(), null)
        );
        this.buffer.push(erase);

        Commander command = this.buffer.getCommanders().get(0);

        assertThat(command).isEqualTo(erase);
    }
}