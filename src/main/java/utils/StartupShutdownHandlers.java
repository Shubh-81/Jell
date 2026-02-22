package utils;

import history.HistoryHandler;

import java.io.IOException;

public class StartupShutdownHandlers {
    public static void onStartup() throws IOException {
        // Enables raw mode in command line, allowing not returning what was typed back directly
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty raw -echo < /dev/tty"});
    }

    public static void onShutDown() throws IOException {
        // Disables raw mode, allowing user's shell to return to normal after execution
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty cooked echo < /dev/tty"});
        // Write command history to file
        HistoryHandler.writeHistory();
    }
}
