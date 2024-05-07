package ssd;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import read.ReadModule;
import write.WriteModule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.FILE_ABSOLUTE_LOCATION;
import static cores.SSDConstraint.RESULT_FILENAME;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ssdFullTest {

    private WriteModule writeModule;
    private ReadModule readModule;

    @Test
    void 기본_생성함수() {
        this.writeModule = new WriteModule();
        this.readModule = new ReadModule();

        assertNotNull(this.writeModule);
        assertNotNull(this.readModule);
    }

    @Test
    void 정상적인_데이터_쓰기() {
        this.writeModule = new WriteModule();
        this.readModule = new ReadModule();

        this.writeModule.write(0, "0x12341234");
        this.readModule.read(0);

        String actual = getReadResult();

        assertThat(actual).isEqualTo("0x12341234");
    }

    private String getReadResult() {
        String result = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME));
            result = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException ignored) {
        }
        return result;
    }
}
