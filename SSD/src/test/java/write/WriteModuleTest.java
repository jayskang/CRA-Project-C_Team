package write;

import cores.ExceptionMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class WriteModuleTest {

    @Mock
    private WriteModule writeModule;

    @BeforeEach
    void setUp() {
        this.writeModule = new WriteModule();
    }

    @Test
    void 기본_생성자_함수() {
        assertNotNull(this.writeModule);
    }

    @Test
    void 주소값이_0_이하일때() {
        int address = -1;
        String value = "0x00000001";

        try {
            this.writeModule.write(address, value);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException.getMessage()).isEqualTo(ExceptionMessage.ILLEGAL_ADDRESS_VALUE_EXCEPTION_MSG);
        }
    }
}