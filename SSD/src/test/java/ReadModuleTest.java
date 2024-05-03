import cores.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadModule;

import static org.assertj.core.api.Assertions.assertThat;

class ReadModuleTest {

    private ReadModule readModule;

    @BeforeEach
    void setUp() {
        readModule = new ReadModule();
    }

    @Test
    void 주소입력범위예외체크() {
        try {
            this.readModule.read("ssd R 192");
        } catch (IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException.getMessage()).isEqualTo(ExceptionMessage.ILLEGAL_ADDRESS_VALUE_EXCEPTION_MSG);
        }
    }

//    @Test
//    void read할때_파일을_읽어오는지_체크(){
//        ReadNandModule readModule = new ReadNandModule();
//        readModule.readFile();
//
//        assertEquals(0,readResult.length);
//    }

}