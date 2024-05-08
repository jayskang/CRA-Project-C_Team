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

        try {
            new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, "C:\\test\\ssd.jar"
                    , SSD_READ_OPTION_CMD, "10").start();

            ssd.setSsdProgramPath("C:\\test\\ssd.jar");

            shell.fullwrite("0x12345678");
            assertEquals("0x12345678", shell.read("10"));

            ArrayList<String> list = shell.fullread();
            for (int i = 0; i < 100; i++) {
                assertEquals("0x12345678", list.get(i));
            }
            assertEquals(100, list.size());
        } catch(Exception e){
            System.out.println("외부 프로그램 존재하지 않아 자동 패스합니다.");
            System.out.println("테스트 전 C:\\test\\ssd.jar 파일을 세팅해주세요.");
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
        assertThatThrownBy(()->{
            shell.erase("100","99");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }

    @Test
    void erase_정상_LBA_정상_SIZE_11이상_모든_영역_ERASE() throws IOException {
        shell.erase("0", "15");
        verify(mockSsd, times(1)).erase("0","10");
        verify(mockSsd, times(1)).erase("10","5");
    }

    @Test
    void erase_LBA_99_정상_SIZE_10_1개_영역_ERASE() throws IOException {
        shell.erase("99", "10");
        verify(mockSsd, times(1)).erase("99","1");
    }

    @Test
    void erase_LBA_95_정상_SIZE_7_5개_영역_ERASE() throws IOException {
        shell.erase("95", "10");
        verify(mockSsd, times(1)).erase("95","5");
    }

    @Test
    void erase_LBA_85_정상_SIZE_17_잔여_5개_영역_ERASE() throws IOException {
        shell.erase("85", "17");
        verify(mockSsd, times(1)).erase("85","10");
        verify(mockSsd, times(1)).erase("95","5");
    }

    @Test
    void erase_LBA_85_정상_SIZE_15_잔여_5개_영역_ERASE() throws IOException {
        shell.erase("85", "15");
        verify(mockSsd, times(1)).erase("85","10");
        verify(mockSsd, times(1)).erase("95","5");
    }

    @Test
    void erase_비정상_LBA_문자() throws IOException {
        assertThatThrownBy(()->{
            shell.erase("A", "10");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);

    }

    @Test
    void erase_비정상_SIZE_문자() throws IOException {
        assertThatThrownBy(()->{
            shell.erase("0", "A");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }

    @Test
    void erase_LBA_95_정상_SIZE_17_5개_영역_ERASE() throws IOException {
        shell.erase("95", "17");
        verify(mockSsd, times(1)).erase("95","5");
    }
    @Test
    void eraserange_정상_Start_LBA_정상_End_LBA() throws IOException {
        shell.eraserange("0", "1");
        verify(mockSsd, times(1)).erase("0","1");

        shell.eraserange("0", "100");
        verify(mockSsd, times(1)).erase("0","100");
    }


    @Test
    void eraserange_음수_Start_LBA_정상_End_LBA() throws IOException {
        assertThatThrownBy(()->{
            shell.eraserange("-1", "1");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);

    }
    @Test
    void eraserange_정상_Start_LBA_음수_End_LBA(){
        assertThatThrownBy(()->{
            shell.eraserange("0", "-1");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }

    @Test
    void eraserange_문자_Start_LBA_정상_End_LBA(){
        assertThatThrownBy(()->{
            shell.eraserange("AA", "1");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }
    @Test
    void eraserange_정상_Start_LBA_문자_End_LBA(){
        assertThatThrownBy(()->{
            shell.eraserange("0", "AA");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }
    @Test
    void eraserange_Start_LBA_End_LBA_값_역전(){
        assertThatThrownBy(()->{
            shell.eraserange("0", "0");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);

        assertThatThrownBy(()->{
            shell.eraserange("1", "1");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }
    @Test
    void eraserange_Null_Start_LBA_Null_End_LBA(){
        assertThatThrownBy(()->{
            shell.eraserange(null, null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }
    @Test
    void eraserange_정상_Start_LBA_100초과_End_LBA(){
        assertThatThrownBy(()->{
            shell.eraserange("0", "101");
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_COMMAND_STRING);
    }
    @Test
    void eraserange_Start_End_분할명령_실행() throws IOException {
        shell.eraserange("0", "11");
        verify(mockSsd, times(1)).erase("0", "10");
        verify(mockSsd, times(1)).erase("10", "1");
    }

    @Test
    void eraserange_Start_End_분할명령_실행_Full() throws IOException {
        shell.eraserange("0", "100");
        for(int i = 0; i < 100; i += 10){
            verify(mockSsd, times(1)).erase(String.valueOf(i), "10");
        }
    }

}
