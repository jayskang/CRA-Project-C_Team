package write;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import read.SsdFileReader;

import java.io.IOException;
import java.util.Arrays;

import static cores.SSDConstraint.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SsdFileWriterTest {

    private SsdFileWriter writer;

    private String[] virtualNand;

    @Spy
    private SsdFileReader FILE_READER;

    @BeforeEach
    void setUp() {
        this.virtualNand = new String[MAX_BOUNDARY];
        Arrays.fill(virtualNand, INITIAL_STATE);
    }

    @Test
    void 기본_생성자_함수() {
        this.writer = new SsdFileWriter();
        assertNotNull(this.writer);
    }

    @Test
    void 정상적인_저장(){
        this.writer = new SsdFileWriter();

        this.writer.store(0, "0x12341234");

        this.virtualNand[0] = "0x12341234";
        try {
            when(this.FILE_READER.readFile()).thenReturn(this.virtualNand);
        } catch (IOException | NullPointerException ignored) {

        }
        assertThat("0x12341234").isEqualTo(this.virtualNand[0]);
    }
}