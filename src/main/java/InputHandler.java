import java.io.BufferedReader;
import java.io.InputStreamReader;

import static utils.Constants.DOLLAR_SIGN;
import static utils.Constants.NEW_LINE;
import static utils.Constants.BACKSPACE;
import com.google.inject.Inject;

public class InputHandler implements BaseInputHandler {

    BaseAutoCompleteHandler autoCompleteHandler;
    BaseCommandHandler commandHandler;
    BufferedReader bufferedReader;

    @Inject
    InputHandler(BaseAutoCompleteHandler autoCompleteHandler, BaseCommandHandler commandHandler) {
        this.autoCompleteHandler = autoCompleteHandler;
        this.commandHandler = commandHandler;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
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

        // Stores current character
        int ch;

        // Helper to break loop from switch case
        boolean terminateCommand = false;

        while (!terminateCommand && ((ch = bufferedReader.read()) != -1)) {
            switch ((char) ch) {

                // Current input terminated by Ctrl + C
                case 3: {
                    terminateCommand = true;
                    System.out.print(NEW_LINE);
                    break;
                }

                // Tab invokes auto complete handler
                case '\t': {
                    // Auto complete prediction from handler
                    String completion = autoCompleteHandler.autoComplete(input);
                    input += completion;

                    // Display completion to user
                    System.out.print(completion);

                    break;
                }

                // Enter invokes command execution
                case '\n':
                case '\r': {
                    // Output will come from new line
                    System.out.print(NEW_LINE);

                    // Send command to command handler
                    commandHandler.handleCommand(input);

                    terminateCommand = true;
                    break;
                }

                // Handle backspace
                case 127:
                case '\b': {
                    // If input is non empty
                    if (!input.isEmpty()) {
                        // Remove last character
                        input = input.substring(0, input.length() - 1);

                        // Shift cursor to left
                        System.out.print(BACKSPACE);
                    }

                    break;
                }

                // Rest characters are printed in console and added to input
                default: {
                    System.out.print((char) ch);
                    input += (char) ch;
                }
            }
        }
    }

    public void work() {
        loop();
    }
}
