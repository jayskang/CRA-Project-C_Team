package command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommanderTest {
    @Test
    void noArgumentTest() {
        String[] args = {};

        assertThrows(IllegalArgumentException.class, () -> {
            Commander commander = new Commander(args);
        });
    }

    @Test
    void over3ArgumentTest() {
        String[] args = {"W", "0", "0x00000001", "0x00000001"};

        assertThrows(IllegalArgumentException.class, () -> {
            Commander commander = new Commander(args);
        });
    }

    @Test
    void invalidCommandNameTest() {
        String[] args = {"A", "0x00000000"};
        Commander commander = new Commander(args);

        assertThrows(IllegalArgumentException.class, commander::runCommand);

    }

    @Test
    void writeCommandButNoInputData() {
        String [] args = {"W", "0"};

        assertThrows(IllegalArgumentException.class, () -> {
            Commander commander = new Commander(args);
        });
    }
}