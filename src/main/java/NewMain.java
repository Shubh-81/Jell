import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

public class NewMain {
    private static void onStartup() throws IOException {
        // Enables raw mode in command line, allowing not returning what was typed back directly
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty raw -echo < /dev/tty"});
    }

    private static void onShutDown() throws IOException {
        // Disables raw mode, allowing user's shell to return to normal after execution
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty cooked echo < /dev/tty"});
    }

    public static void main(String[] args) throws Exception {
        onStartup();

        // Creates injector based on bindings in ShellModule
        Injector injector = Guice.createInjector(new ShellModule());
        BaseInputHandler inputHandler = injector.getInstance(BaseInputHandler.class);

        inputHandler.work();

        onShutDown();
    }
}
