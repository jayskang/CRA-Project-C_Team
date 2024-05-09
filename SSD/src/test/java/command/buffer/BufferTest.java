package command.buffer;

import command.Buffer;
import command.Commander;
import cores.SSDConstraint;
import erase.EraseModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    void 같은_LBA를_가지는_W_명령어가_여러개_입력되었을때() throws IOException {
        this.buffer.push(createCommand("W", "0", "0x11111111"));
        this.buffer.push(createCommand("W", "1", "0x22222222"));
        this.buffer.push(createCommand("W", "3", "0x33333333"));
        this.buffer.push(createCommand("W", "1", "0x55555555"));
        this.buffer.push(createCommand("W", "4", "0xAAAAAAAA"));
        this.buffer.push(createCommand("W", "4", "0xBBBBBBBB"));
        this.buffer.push(createCommand("W", "1", "0xEEEEEEEE"));
        this.buffer.push(createCommand("W", "0", "0xFFFFFFFF"));

        String[] checkLbaList = new String[]{"0", "1", "4"};
        String[] values = new String[]{"0xFFFFFFFF", "0xEEEEEEEE", "0xBBBBBBBB"};
        for (int i = 0; i < 3; i += 1) {
            boolean hit = this.buffer.hit(createCommand("R", checkLbaList[i], null));

            if (hit) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(SSDConstraint.RESULT_FILENAME));

                assertThat(bufferedReader.readLine()).isEqualTo(values[i]);
            } else {
                fail();
            }
        }
        assertThat(this.buffer.getCommanders().size()).isEqualTo(4);
    }

    @Test
    void R_명령어가_HIT_일때() {
        this.buffer.push(createCommand("W", "0", "0x11111111"));
        boolean actual = this.buffer.hit(createCommand("R", "0", null));

        assertThat(actual).isEqualTo(true);
    }

    @Test
    void R_명령어가_MISS_일때() {
        this.buffer.push(createCommand("W", "0", "0x11111111"));
        boolean actual = this.buffer.hit(createCommand("R", "1", null));

        assertThat(actual).isEqualTo(false);
    }

    @Test
    void W_명령어_입력시_버퍼에_E_명령어_재조정() {
        this.buffer.push(createCommand("E", "0", "2"));
        this.buffer.push(createCommand("W", "0", "0x11111111"));

        ArrayList<Commander> commands = this.buffer.getCommanders();

        Commander e = commands.get(0);
        Commander w = commands.get(1);

        assertThat(Commander.ERASE).isEqualTo(e.getCommand());
        assertThat(Commander.WRITE).isEqualTo(w.getCommand());
    }

    private Commander createCommand(String type, String lba, String value) {
        switch (type) {
            case "W":
                return new Commander(new String[]{Commander.WRITE, lba, value},
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