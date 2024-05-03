import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import read.ReadModule;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WriteModuleTest {

}

class ReadModuleTest {
    @Test
    void 주소입력범위예외체크() {
        ReadModule readModule = new ReadModule();
        assertThrows(RuntimeException.class, () -> {
            readModule.read("ssd R 192");
        });
    }


}