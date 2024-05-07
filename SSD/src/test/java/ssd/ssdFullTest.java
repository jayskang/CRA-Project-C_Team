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
    public static final int EXITST_VALUE_ADDRESS = 20;
    public static final int NULL_VALUE_ADDRESS = 10;

    private WriteCore writeCore;
    private ReadCore readCore;


    @BeforeEach
    void setUp() {
        writeCore = new WriteModule();
        readCore = new ReadModule();
    }

    @Test
    void 입력한_주소_Value_출력() throws IOException {
        writeCore.write(EXITST_VALUE_ADDRESS, SAMPLE_VALUE);
        readCore.read(EXITST_VALUE_ADDRESS);

        assertEquals(SAMPLE_VALUE,
                new BufferedReader(new FileReader(new File(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME)))
                        .readLine());
    }

    @Test
    void 입력하지않은_주소_Value_출력() throws IOException {
        writeCore.write(EXITST_VALUE_ADDRESS, SAMPLE_VALUE);
        readCore.read(NULL_VALUE_ADDRESS);

        assertEquals(DEFAULT_VALUE,
                new BufferedReader(new FileReader(new File(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME)))
                        .readLine());

    }

    @Test
    void write하지않고_read() throws IOException {
        readCore.read(NULL_VALUE_ADDRESS);

        assertEquals(DEFAULT_VALUE,
                new BufferedReader(new FileReader(new File(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME)))
                        .readLine());

    }

    @Test
    void nand_파일_미존재() throws IOException {
        deleteNandFile();

        readCore.read(NULL_VALUE_ADDRESS);

        assertEquals(DEFAULT_VALUE,
                new BufferedReader(new FileReader(new File(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME)))
                        .readLine());

    }

    private static void deleteNandFile() {
        File file = new File(FILE_ABSOLUTE_LOCATION + NAND_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

}
