import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import read.ReadCore;
import read.ReadModule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadModuleTest {

    @Spy
    private ReadModule readModule;


    @Test
    void 주소입력범위예외체크() {
        ReadModule readModule = this.readModule;
        readModule.read("192");
        verify(readModule,times(1)).isValidAddress("192");
    }


}