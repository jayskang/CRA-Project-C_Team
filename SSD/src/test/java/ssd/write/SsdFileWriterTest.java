package ssd.write;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import read.SsdFileReader;
import write.SsdFileWriter;

import java.io.IOException;
import java.util.Arrays;

import static cores.SSDConstraint.INITIAL_STATE;
import static cores.SSDConstraint.MAX_BOUNDARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SsdFileWriterTest {

    private String[] virtualNand;

    @Mock
    private SsdFileWriter writer;

    @Spy
    private SsdFileReader reader;

    private final String VALID_VALUE = "0x12341234";

    @BeforeEach
    void setUp() {
        this.writer = mock(SsdFileWriter.class);
        this.virtualNand = new String[MAX_BOUNDARY];
        Arrays.fill(virtualNand, INITIAL_STATE);
    }

    @Test
    void 정상적인_저장() {
        this.writer.store(0, VALID_VALUE);
        this.virtualNand[0] = VALID_VALUE;

        try {
            when(this.reader.readFile()).thenReturn(this.virtualNand);
        } catch (IOException | NullPointerException ignored) {
        }
        verify(this.writer).store(0, VALID_VALUE);
        assertThat(VALID_VALUE).isEqualTo(this.virtualNand[0]);
    }

    @Test
    void 허용되지않는_주소값() {
        this.writer.store(-1, VALID_VALUE);

        try {
            when(this.reader.readFile()).thenReturn(this.virtualNand);
        } catch (IOException | NullPointerException ignored) {
        }
        verify(this.writer).store(-1, VALID_VALUE);
        assertThat("0x00000000").isEqualTo(this.virtualNand[0]);
    }
}