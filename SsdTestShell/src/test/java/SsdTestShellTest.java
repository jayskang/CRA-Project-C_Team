import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;

import static constants.Command.*;
import static constants.Messages.ERROR_MSG_RESULT_FILE_NOT_FOUNDED;
import static constants.Messages.ERROR_MSG_SSD_CANNOT_EXEC;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SsdTestShellTest {
    public static final String NORMAL_DATA = "0x12345678";
    public static final String NORMAL_LBA = "3";
    public static final String INVALID_COMMAND_STRING = "INVALID COMMAND";
    @Mock
    SSDExecutor mockSsd;
    @Mock
    SSDResultFileReader resultFileReader;
    @Spy
    SSDExecutor spySsd;
    @Spy
    private SsdTestShell shell;

    @BeforeEach
    void setUp() {
        shell.setSsd(mockSsd);
    }

    @Test
    void read_함수_LBA_문자열_정상인_경우() throws IOException {
        shell.read("1");
        verify(mockSsd, times(1)).read("1");
    }
  
    @Test
    void SsdTestShell_객체_정상적으로_생성() {
        assertNotNull(shell);
    }
  
    @Test
    void read_함수_LBA_문자열_음수인_경우(){
        assertThatThrownBy(()->{
            shell.read("-1");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID COMMAND");
    }

    @Test
    void read_함수_LBA_문자열_99_초과인_경우(){
        assertThatThrownBy(()->{
            shell.read("100");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID COMMAND");
    }

    @Test
    void read_함수_LBA_문자열_정수가_아닌_경우(){
        assertThatThrownBy(()->{
            shell.read("A");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID COMMAND");
    }

    @Test
    void read_함수_LBA_문자열_null인_경우(){
        assertThatThrownBy(()->{
            shell.read(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("INVALID COMMAND");
    }

    @Test
    void write_함수_testShell의_write가_호출되면_mockSsd의_write_호출() {
        try {
            shell.write(NORMAL_LBA, NORMAL_DATA);

            verify(mockSsd, times(1)).write(NORMAL_LBA, NORMAL_DATA);
        } catch(Exception e) {

        }
    }

    @Test
    void write_함수_data가_0x로_시작하지_않으면_예외처리() {
        assertThrows(IllegalArgumentException.class, () -> {
            shell.write(NORMAL_LBA, "1234567800");
        });
    }

    @Test
    void write_함수_data의_길이가_10이_아니면_예외처리() {
        assertThrows(IllegalArgumentException.class, () -> {
            shell.write(NORMAL_LBA, "0x1234567");
        });
    }

    @Test
    void write_함수_lba에_숫자가_아닌_값이_들어오면_예외처리() {
        String[] nonNumericStrs = {"not number", "include number 0", "", null};

        for (String nonNumeric : nonNumericStrs) {
            assertThrows(IllegalArgumentException.class, () -> {
                shell.write(nonNumeric, NORMAL_DATA);
            });
        }
    }

    @Test
    void write_함수_data에_숫자가_아닌_값이_들어오면_예외처리() {
        String[] nonNumericStrs = {"not number", "include number 0", "", null};

        for (String nonNumeric : nonNumericStrs) {
            assertThrows(IllegalArgumentException.class, () -> {
                shell.write(NORMAL_LBA, nonNumeric);
            });
        }
    }

    @Test
    void fullWrite_함수_정상_호출시_write함수_100번_호출() {
        try {
            shell.fullwrite(NORMAL_DATA);

            verify(mockSsd, times(100)).write(anyString(), eq(NORMAL_DATA));
        } catch (Exception e) {

        }
    }

    @Test
    void fullWrite_함수_data값이_유효하지_않다면_예외발생() {
        assertThrows(IllegalArgumentException.class, () -> {
            shell.fullwrite("0x1234");
        });
    }

    @Test
    void shell_Read_함수_파일사용_출력_결과() throws IOException {
        shell.setSsd(spySsd);
        doReturn("0 0x11111111").when(spySsd).readResultFile();

        shell.read("0");

        verify(spySsd, times(1)).execSsdReadCommand("0");
        verify(spySsd, times(1)).readResultFile();
    }

    @Test
    void ssd_Read_함수_명령실행_및_파일읽기() throws IOException {
        // invalid argument일 경우 ssd의 read함수는 호출되지 않는다.
        doReturn("0 0x11111111").when(spySsd).readResultFile();

        spySsd.read("0");

        verify(spySsd, times(1)).execSsdReadCommand("0");
        verify(spySsd, times(1)).readResultFile();
    }

    @Test
    void ssd_readResultFile_함수() throws IOException {
        SSDExecutor ssd = new SSDExecutor();

        ssd.setResultFileReader(new SSDResultFileReader());

        System.out.println(ssd.readResultFile());
    }

    @Test
    void ssd_Read_함수_파일읽기_실패() throws IOException {
        spySsd.setResultFileReader(resultFileReader);
        doThrow(new IOException()).when(resultFileReader).readFile();

        assertThatThrownBy(()->{
            spySsd.readResultFile();
        }).isInstanceOf(IOException.class)
                .hasMessageContaining(ERROR_MSG_RESULT_FILE_NOT_FOUNDED);
    }

    @Test
    void ssd_fullread_명령시_readFile_호출_횟수() throws IOException {
        spySsd.setResultFileReader(resultFileReader);
        shell.setSsd(spySsd);
        for(int i = 0; i < 100; i++){
            when(spySsd.read(String.valueOf(i))).thenReturn(String.valueOf(i));
        }
        shell.fullread();
        verify(resultFileReader, times(100)).readFile();
    }

    @Test
    void 외부_프로그램_실행_기능_테스트() throws IOException {
        // 임시 jar파일 생성 후 로컬에서 테스트 필요. 임시 테스트 함수. 삭제해야 함.
        SSDExecutor ssd = new SSDExecutor();
        ssd.execSsdReadCommand("1");
    }

    @Test
    void 외부_프로그램_실행_기능_테스트2() throws IOException {
        // 임시 jar파일 생성 후 로컬에서 테스트 필요. 임시 테스트 함수. 삭제해야 함.
        SSDExecutor ssd = new SSDExecutor();
        ssd.setResultFileReader(resultFileReader);

        ssd.write("10", "0x12345678");

        System.out.println(ssd.read("1"));
    }

    @Test
    void sdd_write_함수_명령실행() throws IOException{
        spySsd.write(NORMAL_LBA, NORMAL_DATA);

        verify(spySsd, times(1)).execSsdWriteCommand(NORMAL_LBA, NORMAL_DATA);
    }

    @Test
    void Time_Wait_없이_fullread_명령과_result_읽기가_순차적으로_진행되는가() throws IOException, InterruptedException {
        SsdTestShell shell = new SsdTestShell();
        SSDExecutor ssd = new SSDExecutor();
        SSDResultFileReader reader = new SSDResultFileReader();
        shell.setSsd(ssd);
        ssd.setResultFileReader(reader);
        // ssd jar파일이 준비되지 않으면 자동 패스
        try {
            // 파일이 없으면 이 테스트는 그냥 종료토록 함
            new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, "C:\\test\\ssd.jar"
                    , SSD_EXEC_READ_OPTION, "10").start();

            ssd.setSsdProgramPath("C:\\test\\ssd.jar");
            reader.setResultFilePath("C:\\test\\result.txt");

            shell.fullwrite("0xFFFFFFFE");
            assertEquals("10 0xFFFFFFFE", shell.read("10"));

            Thread.sleep(100);

            ArrayList<String> list = shell.fullread();
            assertEquals(100, list.size());
        } catch(Exception e){
            System.out.println("외부 프로그램 존재하지 않아 자동 패스합니다.");
        }
    }

    @Test
    void erase_정상_LBA_정상_SIZE_10이하() throws IOException {
        shell.erase("0", "10");
        verify(mockSsd, times(1)).erase("0", "10");
    }
    @Test
    void erase_정상_LBA_비정상_SIZE_음수() throws IOException {
        assertThatThrownBy(()->{
            shell.erase("0","-1");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }
    @Test
    void erase_정상_LBA_비정상_SIZE_0값(){
        assertThatThrownBy(()->{
            shell.erase("0","0");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }
    @Test
    void erase_비정상_LBA_음수(){
        assertThatThrownBy(()->{
            shell.erase("-1","1");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }
    @Test
    void erase_비정상_LBA_100이상(){

    }
    @Test
    void erase_정상_LBA_정상_SIZE_11이상(){

    }
    @Test
    void erase_SIZE가_LBA_MAX초과시_처리(){

    }
    @Test
    void erase_비정상_LBA_문자() throws IOException {
        shell.erase("0", "10");
        verify(mockSsd, times(1)).erase("0", "10");
    }
}
