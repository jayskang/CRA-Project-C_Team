package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import read.ReadCore;
import read.ReadModule;
import write.WriteCore;
import write.WriteModule;

import java.io.*;

import static cores.SSDConstraint.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ssdFullTest {

    public static final String SAMPLE_VALUE = "0x1289CDEF";
    public static final int EXITST_VALUE_LBA = 20;
    public static final int NULL_VALUE_LBA = 10;

    private WriteCore writeCore;
    private ReadCore readCore;


    @BeforeEach
    void setUp() {
        writeCore = new WriteModule();
        readCore = new ReadModule();
    }

    @Test
    void 입력한_주소_Value_출력() throws IOException {
        writeCore.write(EXITST_VALUE_LBA, SAMPLE_VALUE);
        readCore.read(EXITST_VALUE_LBA);

        assertEquals(SAMPLE_VALUE, getReadResult());
    }

    @Test
    void 입력하지않은_주소_Value_출력() throws IOException {
        writeCore.write(EXITST_VALUE_LBA, SAMPLE_VALUE);
        readCore.read(NULL_VALUE_LBA);

        assertEquals(DEFAULT_VALUE, getReadResult());

    }

    @Test
    void write하지않고_read() throws IOException {
        readCore.read(NULL_VALUE_LBA);

        assertEquals(DEFAULT_VALUE, getReadResult());

    }

    @Test
    void nand_파일_미존재() throws IOException {
        deleteNandFile();

        readCore.read(NULL_VALUE_LBA);

        assertEquals(DEFAULT_VALUE, getReadResult());

    }

    private static String getReadResult() throws IOException {
        return new BufferedReader(new FileReader(
                (FILE_ABSOLUTE_LOCATION + RESULT_FILENAME)))
                .readLine();
    }

    private static void deleteNandFile() {
        File file = new File(FILE_ABSOLUTE_LOCATION + NAND_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

}
