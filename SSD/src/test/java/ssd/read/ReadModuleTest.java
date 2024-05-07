package ssd.read;

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

    public static final String SAMPLE_VALUE = "0x1289CDEF";
    public static final int EXITST_VALUE_ADDRESS = 20;
    public static final int NULL_VALUE_ADDRESS = 10;

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

        assertEquals(SAMPLE_VALUE, fileReadResult[EXITST_VALUE_ADDRESS]);
    }

    @Test
    void 호출한_주소의_값이_없을때_파일read() throws IOException {
        writeAllAddressToNandFile();

        fileReadResult = ssdFileReader.readFile();

        assertEquals(null, fileReadResult[1]);
    }

    @Test
    void 결과파일생성여부확인() {
        File resultfile = new File(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME);

        assertNotNull(resultfile.exists());
    }

    @Test
    void 결과파일에_값이_있을때() throws IOException {
        writeAllAddressToNandFile();

        readModule.read(EXITST_VALUE_ADDRESS);

        assertEquals(SAMPLE_VALUE,
                new BufferedReader(new FileReader(new File(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME)))
                        .readLine());
    }

    @Test
    void 결과파일에_값이_없을때() throws IOException {
        writeAllAddressToNandFile();

        readModule.read(NULL_VALUE_ADDRESS);

        assertEquals(DEFAULT_VALUE,
                new BufferedReader(new FileReader(new File(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME)))
                        .readLine());
    }

    private void createDefaultNandFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_ABSOLUTE_LOCATION + NAND_FILENAME), false));
        for (int address = 0; address < MAX_BOUNDARY; address++) {
            writer.write(address + " \n");
        }
        writer.close();
    }

    private static String[] setArrayWithNull() {
        String expected[] = new String[MAX_BOUNDARY];
        for (int index = 0; index < MAX_BOUNDARY; index++) {
            expected[index] = null;
        }
        return expected;
    }

    private void writeAllAddressToNandFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_ABSOLUTE_LOCATION + NAND_FILENAME), false));
        for (int address = 0; address < MAX_BOUNDARY; address++) {
            if (address == EXITST_VALUE_ADDRESS) {
                writer.write(address + " " + SAMPLE_VALUE + "\n");
                continue;
            }
            writer.write(address + " \n");
        }
        writer.close();
    }
}