import java.util.HashMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.MockedStatic;
import utils.SystemProperties;

import static org.mockito.Mockito.mockStatic;

public class AutoCompleteHandlerTest {
    @Test
    void testAutoCompletion() {
        HashMap<String, String> executables = new HashMap<>();

        executables.put("echo", "/bin/echo");
        executables.put("echh", "/usr/bin/echh");
        executables.put("exit", "/bin/exit");
        executables.put("donut", "/usr/local/bin/donut");

        MockedStatic<SystemProperties> mocked = mockStatic(SystemProperties.class);
        mocked.when(SystemProperties::getExecutables)
                .thenReturn(executables);

        AutoCompleteHandler autoCompleteHandler = new AutoCompleteHandler();

        String ex = autoCompleteHandler.autoComplete("e");
        assertEquals("", ex);

        ex = autoCompleteHandler.autoComplete("ec");
        assertEquals("h", ex);

        ex = autoCompleteHandler.autoComplete("ex");
        assertEquals("it ", ex);
    }
}
