import static utils.Constants.DOLLAR_SIGN;
import static utils.Constants.NEW_LINE;
import static utils.Constants.BACKSPACE;

import com.google.inject.Inject;
import history.BaseHistoryHandler;
import keyParser.BaseKeyParser;
import keys.ArrowKey;
import keys.BaseKey;
import keys.CharKey;
import keys.ControlKey;
import utils.Direction;

public class InputHandler implements BaseInputHandler {

    BaseAutoCompleteHandler autoCompleteHandler;
    BaseCommandHandler commandHandler;
    BaseHistoryHandler historyHandler;
    BaseKeyParser keyParser;

    @Inject
    InputHandler(BaseAutoCompleteHandler autoCompleteHandler, BaseCommandHandler commandHandler, BaseHistoryHandler historyHandler, BaseKeyParser keyParser) {
        this.autoCompleteHandler = autoCompleteHandler;
        this.commandHandler = commandHandler;
        this.historyHandler = historyHandler;
        this.keyParser = keyParser;
    }

    // Loops to keep on taking user input
    private void loop() {
        while (true) {
            try {
                handleInput();
            } catch (Exception e) {
                System.out.print(e.getMessage() + NEW_LINE);
            }
        }
    }

    private void handleInput() throws Exception {
        // Start new input
        System.out.print(DOLLAR_SIGN + " ");

        // Stores user input
        String input = "";

        // Helper to break loop from switch case
        boolean terminateCommand = false;

        while (!terminateCommand) {

            BaseKey key = keyParser.readKey();

            switch (key) {

                case ArrowKey ak -> {
                    switch (ak.direction()) {
                        // Move up in history
                        case Direction.UP -> {
                            input = historyHandler.handleUp(input);
                        }
                        // Move down in history
                        case Direction.DOWN -> {
                            input = historyHandler.handleDown(input);
                        }
                    }
                }

                // Current input terminated by Ctrl + C
                case ControlKey.CTRL_C -> {
                    terminateCommand = true;
                    System.out.print(NEW_LINE);

                    historyHandler.resetIndex();
                }

                // Tab invokes auto complete handler
                case ControlKey.TAB -> {
                    // Auto complete prediction from handler
                    String completion = autoCompleteHandler.autoComplete(input);
                    input += completion;

                    // Display completion to user
                    System.out.print(completion);
                }

                // Enter invokes command execution
                case ControlKey.ENTER -> {
                    // Output will come from new line
                    System.out.print(NEW_LINE);

                    // Store current command
                    historyHandler.recordCommand(input);

                    // Send command to command handler
                    commandHandler.handleCommand(input);

                    terminateCommand = true;
                }

                // Handle backspace
                case ControlKey.BACKSPACE -> {
                    // If input is non-empty
                    if (!input.isEmpty()) {
                        // Remove last character
                        input = input.substring(0, input.length() - 1);

                        // Shift cursor to left
                        System.out.print(BACKSPACE);
                    }
                }

                // Default character keys
                case CharKey ck -> {
                    System.out.print(ck.ch());
                    input += ck.ch();
                }

                // Do nothing
                default -> {}
            }
        }
    }

    public void work() {
        loop();
    }
}
