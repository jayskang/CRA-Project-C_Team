package write;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import read.SsdFileReader;

import java.io.IOException;
import java.util.Arrays;

import static cores.SSDConstraint.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SsdFileWriterTest {

    private String[] virtualNand;

    @Mock
    private SsdFileWriter writer;

    @Spy
    private SsdFileReader reader;

    @BeforeEach
    void setUp() {
        this.writer = mock(SsdFileWriter.class);
        this.virtualNand = new String[MAX_BOUNDARY];
        Arrays.fill(virtualNand, INITIAL_STATE);
    }

    @Test
    void 정상적인_저장() {
        this.writer.store(0, "0x12341234");
        this.virtualNand[0] = "0x12341234";

        try {
            when(this.reader.readFile()).thenReturn(this.virtualNand);
        } catch (IOException | NullPointerException ignored) {
        }
        verify(this.writer).store(0, "0x12341234");
        assertThat("0x12341234").isEqualTo(this.virtualNand[0]);
    }
}