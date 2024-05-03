import cores.AddressConstraint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import read.ReadModule;
import read.SsdFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadModuleTest {

    @Spy
    private ReadModule readModule;

    private  File file;
    private FileWriter fw;
    private  SsdFileReader ssdFileReader;
    private String result[];

    @BeforeEach
    void setUp() throws IOException {
        file = new File("nand.txt");
        file.createNewFile();
        fw = new FileWriter(file);

        ssdFileReader = new SsdFileReader();

        result = new String[100];
    }

    @Test
    void 주소입력범위예외체크() {
        ReadModule readModule = this.readModule;
        readModule.read(192);
        verify(readModule,times(1)).isValidAddress(192);
    }

    @Test
    void 빈파일_호출했을때() throws IOException {
        createWriteSampleFile("");
        String expected[] = new String[100];
        for(int index=0;index< AddressConstraint.MAX_BOUNDARY;index++){
            expected[index]=null;
        }

        result = ssdFileReader.readFile();

        assertArrayEquals(expected,result);
    }

    private void createWriteSampleFile(String writeSample) throws IOException {
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(writeSample);
        writer.close();
    }


}