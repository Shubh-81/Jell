import java.util.HashMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import utils.ExecutableProvider;


public class AutoCompleteHandlerTest {
    @Test
    void testAutoCompletion() {
        HashMap<String, String> executables = new HashMap<>();

        executables.put("echo", "/bin/echo");
        executables.put("echh", "/usr/bin/echh");
        executables.put("exit", "/bin/exit");
        executables.put("donut", "/usr/local/bin/donut");

        ExecutableProvider mockProvider = () -> executables;
        AutoCompleteHandler autoCompleteHandler = new AutoCompleteHandler(mockProvider);

        String ex = autoCompleteHandler.autoComplete("e");
        assertEquals("", ex);

        ex = autoCompleteHandler.autoComplete("ec");
        assertEquals("h", ex);

        ex = autoCompleteHandler.autoComplete("ex");
        assertEquals("it ", ex);
    }
}
