package command;

import erase.EraseCore;
import erase.EraseModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import read.ReadCore;
import read.ReadModule;
import write.WriteCore;
import write.WriteModule;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommanderTest {
    @Mock
    ReadCore readCore;

    @Mock
    WriteCore writeCore;

    @Mock
    EraseCore eraseCore;

    private Commander commander;

    private Commander getCommander(String[] args) {
        return new Commander(args, readCore, writeCore, eraseCore);
    }

    private Commander getRealCommander(String[] args) {
        return  new Commander(args, new ReadModule(), new WriteModule(), new EraseModule());
    }

    private void verifySsdNotWork() {
        verify(readCore, never()).bufferRead(anyInt());
        verify(writeCore, never()).write(anyInt(), anyString());
    }

    private void getCommanderAndRun(String[] args) {
        commander = getCommander(args);
        commander.runCommand();
    }

    @Test
    void 입력값이_없을때() {
        getCommanderAndRun(new String[]{});

        verifySsdNotWork();
    }

    @Test
    void 입력값이_3개가_넘을때() {
        getCommanderAndRun(new String[]{"W", "0", "0x00000001", "0x00000001"});

        verifySsdNotWork();
    }

    @Test
    void 정해진_명령어가_아닐떄() {
        getCommanderAndRun(new String[]{"A", "0", "0x00000000"});

        verifySsdNotWork();
    }

    @Test
    void 명령어가_W지만_데이터가_Null일때() {
        getCommanderAndRun(new String[]{"W", "0"});

        verifySsdNotWork();
    }

    @Test
    void 명령어가_W이지만_데이터가_비었을때() {
        getCommanderAndRun(new String[]{"W", "0", ""});

        verifySsdNotWork();
    }

    @Test
    void 정상적인_W명령어_입력됐을때() {
        getCommanderAndRun(new String[]{"W", "0", "0x00000001"});

        verify(writeCore, times(1)).write(0, "0x00000001");
    }

    @Test
    void 정상적인_R명령어_입력됐을때() {
        getCommanderAndRun(new String[]{"R", "0"});

        verify(readCore, times(1)).bufferRead(0);
    }

    @Test
    void 주소가_숫자가_아닐때() {
        getCommanderAndRun(new String[]{"R", "A"});

        verifySsdNotWork();
    }

    @Test
    void 명령어가_E이지만_데이터가_null일때() {
        getCommanderAndRun(new String[]{"E", "0"});

        verify(eraseCore, times(0)).E(anyInt(), anyInt());
    }

    @Test
    void 명령어가_E이지만_데이터가_비었을때() {
        getCommanderAndRun(new String[]{"E", "0", ""});

        verify(eraseCore, times(0)).E(anyInt(), anyInt());
    }

    @Test
    void 명령어가_E이지만_데이터가_숫자가_아닐때() {
        getCommanderAndRun(new String[]{"E", "0", "A"});

        verify(eraseCore, times(0)).E(anyInt(), anyInt());
    }

    @Test
    void 정상적인_E명령어_호출됐을때() {
        getCommanderAndRun(new String[]{"E", "0", "2"});

        verify(eraseCore, times(0)).E(0, 2);
    }

    @Test
    void 실제_EraseModule_호출() {
        getRealCommander(new String[]{"E", "0", "1"}).runCommand();
    }
}