import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.RESULT_FILENAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SsdTest {
    @Test
    void 메인_write_후_read() {
        Ssd.main(new String[]{"W", "0", "0x00000001"});
        Ssd.main(new String[]{"R", "0"});

        String expected = "0x00000001";
        String actual = getReadResult();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 메인_바로_read() {
        File nandFile = new File("nand.txt");
        nandFile.deleteOnExit();

        Ssd.main(new String[]{"R", "0"});

        String expected = "0x00000000";
        String actual = getReadResult();

        assertThat(actual).isEqualTo(expected);
    }

    private String getReadResult() {
        String result = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(RESULT_FILENAME));
            result = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException ignored) {
        }
        return result;
    }
}