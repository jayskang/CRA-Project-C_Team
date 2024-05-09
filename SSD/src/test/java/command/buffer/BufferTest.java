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
        String[] lbaList = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] values = new String[]{
                "0x0000000F",
                "0x00000001",
                "0x00000002",
                "0x00000003",
                "0x00000004",
                "0x00000005",
                "0x00000006",
                "0x00000007",
                "0x00000008",
                "0x00000009",
        };

        for (int i = 0; i < 10; i += 1) {
            this.buffer.push(createCommand("W", lbaList[i], values[i]));
        }

        int actual = this.buffer.getCommanders().size();

        assertThat(actual).isEqualTo(10);
    }

    @Test
    void 같은_LBA를_가지는_W_명령어가_입력되었을때() {
        this.buffer.push(createCommand("W", "0", "0x11111111"));
        this.buffer.push(createCommand("W", "0", "0x22222222"));
        this.buffer.push(createCommand("W", "0", "0x33333333"));
        Commander expected = createCommand("W", "0", "0xFFFFFFFF");

        this.buffer.push(expected);

        Commander actual = this.buffer.getCommanders().get(0);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void R_명령어가_HIT_일때() {
        this.buffer.push(createCommand("W", "0", "0x11111111"));
        boolean actual = this.buffer.hit(createCommand("R", "0", null));

        assertThat(actual).isEqualTo(true);
    }

    private Commander createCommand(String type, String lba, String value) {
        switch (type) {
            case "W":
                return new Commander(new String[]{Commander.READ, lba, value},
                        null, new WriteModule(), null);
            case "E":
                return new Commander(new String[]{Commander.ERASE, lba, value},
                        null, null, new EraseModule());
            case "R":
                return new Commander(new String[]{Commander.READ, lba, value},
                        new ReadModule(), null, null);
        }
        return null;
    }
}