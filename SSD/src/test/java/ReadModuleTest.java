import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import read.ReadModule;
import read.SsdFileReader;

import java.io.*;

import static cores.SSDConstraint.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadModuleTest {

    public static final String sampleValue = "0x1289CDEF";
    public static final String defualtValue = "0x00000000";
    public static final int sampleAddress = 20;

    @Spy
    private ReadModule spyReadModule;

    private SsdFileReader ssdFileReader;
    private String fileReadResult[];
    private ReadModule readModule;

    @BeforeEach
    void setUp() {
        ssdFileReader = new SsdFileReader();
        fileReadResult = new String[100];
        readModule = new ReadModule();
    }

    @Test
    void 주소입력범위예외체크() {
        this.spyReadModule.read(192);
        verify(this.spyReadModule, times(1)).isValidAddress(192);
    }

    @Test
    void 주소값이_모두0인파일_호출했을때_파일read() throws IOException {

        createDefaultNandFile();
        String[] expected = setArrayWithNull();

        fileReadResult = ssdFileReader.readFile();

        assertArrayEquals(expected, fileReadResult);
    }

    @Test
    void 호출한_주소의_값이_있을때_파일read() throws IOException {
        writeAllAddressToNandFile();

        fileReadResult = ssdFileReader.readFile();

        assertEquals(sampleValue, fileReadResult[sampleAddress]);
    }

    @Test
    void 호출한_주소의_값이_없을때_파일read() throws IOException {
        writeAllAddressToNandFile();

        fileReadResult = ssdFileReader.readFile();

        assertEquals(null, fileReadResult[1]);
    }

    @Test
    void 결과파일생성여부확인() {
        File resultfile = new File(RESULT_FILENAME);

        assertNotNull(resultfile.exists());
    }

    @Test
    void 결과파일에_값이_있을때() throws IOException {
        writeAllAddressToNandFile();

        readModule.read(sampleAddress);

        assertEquals(sampleValue,
                new BufferedReader(new FileReader(new File(RESULT_FILENAME)))
                        .readLine());
    }

    @Test
    void 결과파일에_값이_없을때() throws IOException {
        writeAllAddressToNandFile();

        readModule.read(10);

        assertEquals(defualtValue,
                new BufferedReader(new FileReader(new File(RESULT_FILENAME)))
                        .readLine());
    }


    private void createDefaultNandFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(NAND_FILENAME), false));
        for (int address = 0; address < 100; address++) {
            writer.write(address + " \n");
        }
        writer.close();
    }

    private static String[] setArrayWithNull() {
        String expected[] = new String[100];
        for (int index = 0; index < MAX_BOUNDARY; index++) {
            expected[index] = null;
        }
        return expected;
    }

    private void writeAllAddressToNandFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(NAND_FILENAME), false));
        for (int address = 0; address < 100; address++) {
            if (address == sampleAddress) {
                writer.write(address + " 0x1289CDEF\n");
                continue;
            }
            writer.write(address + " \n");
        }
        writer.close();
    }
}