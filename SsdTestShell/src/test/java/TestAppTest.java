import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class TestAppTest {
    @Mock
    SsdTestShell shell;

    @Test
    void TestApp1_정상_동작_테스트() {
        Scenario testApp1 = spy(new TestApp1(shell));

        testApp1.testRun();

        verify(shell, times(1)).fullwrite(anyString());
        verify(shell, times(1)).fullread();
    }

    @Test
    void TestApp1_예외발생시_테스트_실패() {
        Scenario testApp1 = spy(new TestApp1(shell));
        doThrow(new IOException()).when(shell).fullread();

        testApp1.testRun();

        assertFalse(testApp1.isPassed());
    }

    @Test
    void TestApp1_fullread한_값이_0x12345678이_아니면_테스트_실패() {
        Scenario testApp1 = spy(new TestApp1(shell));
        doReturn(new ArrayList<String>(Arrays.asList("0x87654321"))).when(shell).fullread();

        testApp1.testRun();

        assertFalse(testApp1.isPassed());
    }

    @Test
    void TestApp2_정상_동작_테스트() throws IOException {
        Scenario testApp2 = spy(new TestApp2(shell));
        doReturn("0x12345678").when(shell).read(anyString());

        testApp2.testRun();

        verify(shell, times(6 * 31)).write(anyString(), anyString());
        verify(shell, times(6)).read(anyString());
    }

    @Test
    void TestApp2_예외발생시_테스트_실패() throws IOException{
        Scenario testApp2 = spy(new TestApp2(shell));
        doThrow(new IOException()).when(shell).read(anyString());

        testApp2.testRun();

        assertFalse(testApp2.isPassed());
    }

    @Test
    void TestApp2_read한_값이_0x12345678이_아니면_테스트_실패() throws IOException{
        Scenario testApp2 = spy(new TestApp2(shell));
        doReturn("0x87654321").when(shell).read(anyString());

        testApp2.testRun();

        assertFalse(testApp2.isPassed());
    }
}