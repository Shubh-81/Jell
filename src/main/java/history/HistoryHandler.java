package history;

import com.google.inject.Singleton;

import java.util.ArrayList;

@Singleton
public class HistoryHandler implements BaseHistoryHandler {
    private static ArrayList<String> previousCommands;

    public HistoryHandler() {
        previousCommands = new ArrayList<>();
    }

    public static ArrayList<String> getPreviousCommands() {
        return previousCommands;
    }

    @Override
    public void recordCommand(String command) {
        previousCommands.add(command);
    }
}
