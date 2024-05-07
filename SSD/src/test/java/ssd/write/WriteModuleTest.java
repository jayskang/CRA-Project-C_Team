package ssd.write;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import read.SsdFileReader;
import write.WriteModule;

import java.io.IOException;
import java.util.Arrays;

import static cores.SSDConstraint.DEFAULT_VALUE;
import static cores.SSDConstraint.MAX_BOUNDARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteModuleTest {

    private final String COLLECT_VALUE = "0x00000001";
    private final String WRONG_FORMAT_VALUE = "00000001";
    private final String NOT_ALLOWED_VALUE = "0x!@#$%";
    private final int COLLECT_ADDRESS = 0;
    private final int NOT_ALLOWED_ADDRESS = -1;

    private WriteModule writeModule;

    private String[] nand;

    @Mock
    private SsdFileReader ssdFileReader;

    @BeforeEach
    void setUp() {
        this.writeModule = new WriteModule();
        this.nand = createDefaultNand();
    }

    @Test
    void 주소값이_0_미만일때() throws IOException {
        this.writeModule.write(NOT_ALLOWED_ADDRESS, COLLECT_VALUE);

        when(this.ssdFileReader.readFile()).thenReturn(this.nand);

        assertThat(this.ssdFileReader.readFile()).isEqualTo(this.nand);
    }

    @Test
    void 주소값이_100_이상일때() throws IOException {
        this.writeModule.write(MAX_BOUNDARY, COLLECT_VALUE);

        when(this.ssdFileReader.readFile()).thenReturn(this.nand);

        assertThat(this.ssdFileReader.readFile()).isEqualTo(this.nand);
    }

    @Test
    void 저장할_입력값이_포맷을_벗어났을때() throws IOException {
        this.writeModule.write(COLLECT_ADDRESS, WRONG_FORMAT_VALUE);

        when(this.ssdFileReader.readFile()).thenReturn(this.nand);

        assertThat(this.ssdFileReader.readFile()).isEqualTo(this.nand);
    }

    @Test
    void 입력값에_이상한_값이_있을때() throws IOException {
        this.writeModule.write(COLLECT_ADDRESS, NOT_ALLOWED_VALUE);

        when(this.ssdFileReader.readFile()).thenReturn(this.nand);

        assertThat(this.ssdFileReader.readFile()).isEqualTo(this.nand);}

    @Test
    void 값이_제대로_저장되었는지_테스트() throws IOException {
        this.nand[COLLECT_ADDRESS] = "0x1234ABCD";

        when(this.ssdFileReader.readFile()).thenReturn(this.nand);

        this.writeModule.write(COLLECT_ADDRESS, "0x1234ABCD");
        String[] readFileResult = this.ssdFileReader.readFile();

        assertThat(readFileResult[COLLECT_ADDRESS]).isEqualTo("0x1234ABCD");
    }

    private String[] createDefaultNand() {
        String[] nand = new String[MAX_BOUNDARY];
        Arrays.fill(nand, DEFAULT_VALUE);
        return nand;
    }
}