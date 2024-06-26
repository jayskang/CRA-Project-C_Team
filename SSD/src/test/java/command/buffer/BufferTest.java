package command.buffer;

import command.*;
import cores.SSDConstraint;
import erase.EraseModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class BufferTest {

    private Buffer buffer;

    @BeforeEach
    void setUp() {
        this.buffer = Buffer.getInstance();
        buffer.flush();
    }

    @Test
    void 기본_생성자() {
        assertNotNull(this.buffer);
    }

    @AfterEach
    void tearDown() {
        Buffer.resetInstance();
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

        int actual = this.buffer.getCommands().size();

        assertThat(actual).isEqualTo(10);
    }

    @Test
    void 같은_LBA를_가지는_W_명령어가_입력되었을때() {
        this.buffer.push(createCommand("W", "0", "0x11111111"));
        this.buffer.push(createCommand("W", "0", "0x22222222"));
        this.buffer.push(createCommand("W", "0", "0x33333333"));
        Command expected = createCommand("W", "0", "0xFFFFFFFF");

        this.buffer.push(expected);

        Command actual = this.buffer.getCommands().get(0);

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
            boolean hit = this.buffer.hit(Integer.parseInt(checkLbaList[i]));

            if (hit) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(SSDConstraint.RESULT_FILENAME));

                assertThat(bufferedReader.readLine()).isEqualTo(values[i]);
            } else {
                fail();
            }
        }
        assertThat(this.buffer.getCommands().size()).isEqualTo(4);
    }

    @Test
    void R_명령어가_HIT_일때() {
        this.buffer.push(createCommand("W", "0", "0x11111111"));
        boolean actual = this.buffer.hit(0);

        assertThat(actual).isEqualTo(true);
    }

    @Test
    void R_명령어가_MISS_일때() {
        this.buffer.flush();
        this.buffer.push(createCommand("W", "0", "0x11111111"));
        boolean actual = this.buffer.hit(1);

        assertThat(actual).isEqualTo(false);
    }

    @Test
    void W_명령어_입력시_버퍼에_E_명령어_재조정() {
        this.buffer.push(createCommand("E", "0", "2"));
        this.buffer.push(createCommand("W", "0", "0x11111111"));

        ArrayList<Command> commands = this.buffer.getCommands();

        Command e = commands.get(0);
        Command w = commands.get(1);

        assertThat(e).isInstanceOf(EraseCommand.class);
        assertThat(w).isInstanceOf(WriteCommand.class);
    }

    @Test
    void W_명령어_입력시_버퍼에_E_명령어가_2개로_나눠질때_재조정() {
        this.buffer.push(createCommand("E", "0", "3"));
        this.buffer.push(createCommand("W", "1", "0x11111111"));

        ArrayList<Command> commands = this.buffer.getCommands();

        Command e1 = commands.get(0);
        Command e2 = commands.get(1);
        Command w = commands.get(2);

        assertThat(e1).isInstanceOf(EraseCommand.class);
        assertThat(e2).isInstanceOf(EraseCommand.class);
        assertThat(w).isInstanceOf(WriteCommand.class);
    }

    @Test
    void E_명령어_DIRTY_초기화_동작검사() {
        this.buffer.push(createCommand("W", "1", "0x11111111"));
        this.buffer.push(createCommand("W", "2", "0x22222222"));
        this.buffer.push(createCommand("W", "3", "0x33333333"));

        Command erase = createCommand("E", "1", "3");

        this.buffer.push(erase);

        ArrayList<Command> commands = this.buffer.getCommands();

        assertThat(commands.size()).isEqualTo(1);
        assertThat(commands.get(0)).isEqualTo(erase);
    }

    @Test
    void E_명령어_단순병합() {
        this.buffer.push(createCommand("E", "5", "3"));
        this.buffer.push(createCommand("E", "7", "3"));

        Command actual = this.buffer.getCommands().get(0);

        assertThat(actual.getLba()).isEqualTo(5);
        assertThat(actual.getInputData()).isEqualTo("5");
    }

    @Test
    void E_명령어_연쇄적인_병합() {
        this.buffer.push(createCommand("E", "5", "3"));
        this.buffer.push(createCommand("E", "7", "3"));
        this.buffer.push(createCommand("E", "4", "4"));

        Command actual = this.buffer.getCommands().get(0);

        assertThat(actual.getLba()).isEqualTo(4);
        assertThat(actual.getInputData()).isEqualTo("6");
    }

    @Test
    void E_명령어_복합_병합동작검사() {
        this.buffer.push(createCommand("E", "3", "3"));
        this.buffer.push(createCommand("E", "4", "5"));
        this.buffer.push(createCommand("E", "7", "6"));
        this.buffer.push(createCommand("E", "13", "3"));
        this.buffer.push(createCommand("E", "71", "6"));
        this.buffer.push(createCommand("E", "23", "3"));
        this.buffer.push(createCommand("E", "37", "6"));
        this.buffer.push(createCommand("E", "53", "3"));
        this.buffer.push(createCommand("E", "66", "6"));
        this.buffer.push(createCommand("E", "23", "3"));
        this.buffer.push(createCommand("E", "73", "6"));
        this.buffer.push(createCommand("E", "80", "3"));

        int[] lba = new int[]{3, 13};
        String[] size = new String[]{"10", "3"};

        for (int i = 0; i < 2; i += 1) {
            try {
                Command command = this.buffer.getCommands().get(i);

                assertThat(command.getLba()).isEqualTo(lba[i]);
                assertThat(command.getInputData()).isEqualTo(size[i]);
            } catch (IndexOutOfBoundsException e) {
                fail();
            }
        }
    }

    @Test
    void 교재_최적화_테스트_1() {
        this.buffer.push(createCommand("E", "0", "1"));
        this.buffer.push(createCommand("E", "3", "1"));
        this.buffer.push(createCommand("E", "1", "2"));
    }

    @Test
    void E_W_명령어_삽입() {
        this.buffer.push(createCommand("E", "1", "2"));
        this.buffer.push(createCommand("W", "4", "1"));

        ArrayList<Command> commands = this.buffer.getCommands();
    }

    @Test
    void 교재_최적화_테스트_2() {
//        W 20
//        0xABCDABCD
//        W 21
//        0x12341234
//        E 18 5
        this.buffer.push(createCommand("W", "20", "0xABCDABCD"));
        this.buffer.push(createCommand("W", "21", "0x12341234"));
        this.buffer.push(createCommand("E", "18", "5"));
    }

    @Test
    void 교재_최적화_테스트_3() {
//        W 20
//        0xABCDABCD
//        E 10 2
//        E 12 3
        this.buffer.push(createCommand("W", "20", "0xABCDABCD"));
        this.buffer.push(createCommand("E", "10", "2"));
        this.buffer.push(createCommand("E", "12", "3"));
    }

    @Test
    void E_명령어_병합() {
        this.buffer.push(createCommand("E", "0", "2"));
        this.buffer.push(createCommand("E", "1", "6"));

        ArrayList<Command> commands = this.buffer.getCommands();
    }

    @Test
    void E_명령어_병합2() {
        this.buffer.push(createCommand("E", "0", "10"));
        this.buffer.push(createCommand("E", "1", "6"));

        ArrayList<Command> commands = this.buffer.getCommands();
    }

    @Test
    void 교재_최적화_테스트_4() {
        this.buffer.push(createCommand("E", "10", "4"));
        this.buffer.push(createCommand("E", "40", "5"));
        this.buffer.push(createCommand("W", "12", "0xABCD1234"));
        this.buffer.push(createCommand("W", "13", "0x4BCD5351"));

        this.buffer.flush();
    }

    @Test
    void 교재_최적화_테스트_4_2() {
        this.buffer.push(createCommand("E", "50", "1"));
        this.buffer.push(createCommand("E", "40", "5"));
        this.buffer.push(createCommand("W", "50", "0xABCD1234"));

        System.out.println(this.buffer.getCommands());
        // E 50 1, E 40 5, W 50 -> E 40 5, W 50
    }

    private Command createCommand(String type, String lba, String value) {
        return CommandFactory.getCommand(new String[]{type, lba, value});
    }

}