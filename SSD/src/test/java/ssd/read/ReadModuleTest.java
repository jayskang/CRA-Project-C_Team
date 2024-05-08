package ssd.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
    public static final int EXITST_VALUE_LBA = 20;
    public static final int NULL_VALUE_LBA = 10;
    public static final int OUT_OF_LBA_BOUNDARY = 192;

    @Spy
    private ReadModule spyReadModule;

    @Mock
    private FileWriter fileWriter;

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
        this.spyReadModule.read(OUT_OF_LBA_BOUNDARY);

        verify(this.spyReadModule, times(1)).isNotValidLba(OUT_OF_LBA_BOUNDARY);
    }

    @Test
    void getResult함수호출체크() throws IOException {
        this.spyReadModule.read(EXITST_VALUE_LBA);

        verify(spyReadModule, times(1)).getResult(EXITST_VALUE_LBA);
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
        writeAllLBAToNandFile();

        fileReadResult = ssdFileReader.readFile();

        assertEquals(SAMPLE_VALUE, fileReadResult[EXITST_VALUE_LBA]);
    }

    @Test
    void 호출한_주소의_값이_없을때_파일read() throws IOException {
        writeAllLBAToNandFile();

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
        writeAllLBAToNandFile();

        readModule.read(EXITST_VALUE_LBA);

        assertEquals(SAMPLE_VALUE, getReadResult());
    }

    @Test
    void 결과파일에_값이_없을때() throws IOException {
        writeAllLBAToNandFile();

        readModule.read(NULL_VALUE_LBA);

        assertEquals(DEFAULT_VALUE, getReadResult());
    }

    private void createDefaultNandFile() throws IOException {
        BufferedWriter writer = setNandWriter();
        for (int lba = 0; lba < MAX_BOUNDARY; lba++) {
            writer.write(lba + " \n");
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

    private void writeAllLBAToNandFile() throws IOException {
        BufferedWriter writer = setNandWriter();
        for (int lba = 0; lba < MAX_BOUNDARY; lba++) {
            if (lba == EXITST_VALUE_LBA) {
                writer.write(lba + " " + SAMPLE_VALUE + "\n");
                continue;
            }
            writer.write(lba + " \n");
        }
        writer.close();
    }

    private static String getReadResult() throws IOException {
        return new BufferedReader(new FileReader((FILE_ABSOLUTE_LOCATION + RESULT_FILENAME)))
                .readLine();
    }

    private static BufferedWriter setNandWriter() throws IOException {
        return new BufferedWriter(new FileWriter(
                (FILE_ABSOLUTE_LOCATION + NAND_FILENAME), false));
    }
}