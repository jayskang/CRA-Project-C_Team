package ssd.write;

import cores.AddressConstraint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import read.ReadModule;
import read.SsdFileReader;
import write.WriteModule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteModuleTest {

    private final String COLLECT_VALUE = "0x00000001";
    private final String WRONG_FORMAT_VALUE = "00000001";
    private final String NOT_ALLOWED_VALUE = "!@#$%";
    private final int COLLECT_ADDRESS = 0;
    private final int NOT_ALLOWED_ADDRESS = -1;

    private WriteModule writeModule;

    @Mock
    private ReadModule READ_MODULE;

    @Spy
    private SsdFileReader ssdFileReader;

    @BeforeEach
    void setUp() {
        this.writeModule = new WriteModule();
    }

    @Test
    void 주소값이_0_미만일때() {
        this.writeModule.write(NOT_ALLOWED_ADDRESS, COLLECT_VALUE);

        assertThat(this.writeModule.getErrorLog().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    void 주소값이_100_이상일때() {
        writeModule.write(AddressConstraint.MAX_BOUNDARY, COLLECT_VALUE);

        assertThat(this.writeModule.getErrorLog().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    void 저장할_입력값이_포맷을_벗어났을때() {
        this.writeModule.write(COLLECT_ADDRESS, WRONG_FORMAT_VALUE);

        assertThat(this.writeModule.getErrorLog().getClass()).isEqualTo(NumberFormatException.class);
    }

    @Test
    void 입력값에_이상한_값이_있을때() {
        this.writeModule.write(COLLECT_ADDRESS, NOT_ALLOWED_VALUE);

        assertThat(this.writeModule.getErrorLog().getClass()).isEqualTo(NumberFormatException.class);
    }

    @Test
    void 값이_제대로_저장되었는지_테스트() {
        String[] readFileContents = new String[]{"0x1234ABCD"};

        when(this.ssdFileReader.readFile()).thenReturn(readFileContents);

        this.writeModule.write(COLLECT_ADDRESS, "0x1234ABCD");
        this.READ_MODULE.read(COLLECT_ADDRESS);

        String[] readFileResult = this.ssdFileReader.readFile();

        assertThat(readFileResult[COLLECT_ADDRESS]).isEqualTo("0x1234ABCD");
    }
}