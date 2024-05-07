import cores.SSDConstraint;
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

import static cores.SSDConstraint.RESULT_FILENAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadModuleTest {

     @Spy
    private ReadModule readModule;

    private File file;
    private FileWriter fileWriter;
    private SsdFileReader ssdFileReader;
    private String fileReadResult[];

    @BeforeEach
    void setUp() throws IOException {
        createNandSampleFile();

        ssdFileReader = new SsdFileReader();
        fileReadResult = new String[100];
    }

    @Test
    void 주소입력범위예외체크() {
        ReadModule readModule = this.readModule;
        readModule.read(192);
        verify(readModule, times(1)).isValidAddress(192);
    }

    @Test
    void 주소값이_모두0인파일_호출했을때_파일read() throws IOException {
        String[] expected = setArrayWithNull();

        fileReadResult = ssdFileReader.readFile();

        assertArrayEquals(expected, fileReadResult);
    }

    @Test
    void 호출한_주소의_값이_있을때_파일read() throws IOException {
        writeAllAddressToNandFile();

        fileReadResult = ssdFileReader.readFile();

        assertEquals("0x1289CDEF", fileReadResult[20]);
    }

    @Test
    void 호출한_주소의_값이_없을때_파일read() throws IOException {
        writeAllAddressToNandFile();

        fileReadResult = ssdFileReader.readFile();

        assertEquals(null, fileReadResult[1]);
    }

    @Test
    void 결과파일생성여부확인(){
        File resultfile = new File(RESULT_FILENAME);

        assertNotNull(resultfile.exists());
    }

    private void createNandSampleFile() throws IOException {
        file = new File(SSDConstraint.FILENAME);
        file.createNewFile();
        fileWriter = new FileWriter(file);
        createNullFile();
    }


    private void createNullFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(fileWriter);
        for (int address = 0; address < 100; address++) {
            writer.write(address + " \n");
        }
        writer.flush();
    }

    private static String[] setArrayWithNull() {
        String expected[] = new String[100];
        for (int index = 0; index < SSDConstraint.MAX_BOUNDARY; index++) {
            expected[index] = null;
        }
        return expected;
    }

    private void writeAllAddressToNandFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(fileWriter);
        for (int address = 0; address < 100; address++) {
            if (address == 20) {
                writer.write(address + " 0x1289CDEF\n");
                continue;
            }
            writer.write(address + " \n");
        }
        writer.flush();
    }
}