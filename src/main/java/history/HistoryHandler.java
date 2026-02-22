package history;

import com.google.inject.Singleton;

import java.util.ArrayList;

import static utils.Constants.BACKSPACE;
import static utils.Constants.BELL_CHAR;

@Singleton
public class HistoryHandler implements BaseHistoryHandler {
    private static ArrayList<String> previousCommands;
    // Stores current unsaved command
    private static String currentCommand;
    // Stores current index to be returned
    private static int index = 0;
    // Stores index till which history is appended
    private static int appendIndex = 0;

    public HistoryHandler() {
        previousCommands = new ArrayList<>();
    }

    public static ArrayList<String> getPreviousCommands() {
        return previousCommands;
    }

    public static int getAppendIndex() {
        return appendIndex;
    }

    public static void updatedAppendIndex() {
        appendIndex = previousCommands.size();
    }

    public static void addCommand(String command) {
        previousCommands.add(command);
    }

    @Override
    public void recordCommand(String command) {
        index = 0;
        previousCommands.add(command);
    }

    private void clearCommand(String command) {
        for (int index = 0; index < command.length(); index++) {
            System.out.print(BACKSPACE);
        }
    }

    public void resetIndex() {
        index = 0;
    }

    public String handleUp(String currentCommand) {
        // If no commands are present, ring bell
        if (previousCommands.isEmpty()) {
            System.out.print(BELL_CHAR);
            return currentCommand;
        }

        // Store currently unsaved command
        if (index == 0) {
            HistoryHandler.currentCommand = currentCommand;
        }
        index += 1;

        // If no commands are left, ring bell
        if (previousCommands.size() < index) {
            index -= 1;
            System.out.print(BELL_CHAR);
            return currentCommand;
        }

        // Clear current command from screen
        clearCommand(currentCommand);

        String prevCommand = previousCommands.get(previousCommands.size() - index);
        System.out.print(prevCommand);

        return prevCommand;
    }

    public String handleDown(String currentCommand) {
        // If we can't go down, print bell character
        if (index == 0) {
            System.out.print(BELL_CHAR);
            return currentCommand;
        }

        // Clear current command from screen
        clearCommand(currentCommand);

        index -= 1;
        String nextCommand;

        // If no commands are left, ring bell
        if (previousCommands.size() < index) {
            index += 1;
            System.out.print(BELL_CHAR);
            return currentCommand;
        }

        if (index == 0) {
            nextCommand = HistoryHandler.currentCommand;
        } else {
            nextCommand = previousCommands.get(previousCommands.size() - index);
        }

        System.out.print(nextCommand);
        return nextCommand;
    }
}
