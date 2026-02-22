package keyParser;

import com.google.inject.Singleton;
import keys.ArrowKey;
import keys.BaseKey;
import keys.CharKey;
import keys.ControlKey;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static utils.Constants.BACKSPACE_CHAR;
import static utils.Constants.BACKSPACE_NUMERIC;
import static utils.Constants.BRACKET;
import static utils.Constants.CARRIAGE;
import static utils.Constants.CHAR_A;
import static utils.Constants.CHAR_B;
import static utils.Constants.CTRL_C;
import static utils.Constants.ENTER;
import static utils.Constants.ESCAPE;
import static utils.Constants.TAB;

@Singleton
public class KeyParser implements BaseKeyParser {
    private final BufferedReader reader;

    KeyParser() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public BaseKey readKey() throws Exception {
        int ch = reader.read();

        // Handle escape sequences
        if ((char) ch == ESCAPE) {
            if (reader.ready()) {
                int escapeCharacter = reader.read();
                if ((char) escapeCharacter == BRACKET) {
                    int directionCharacter = reader.read();
                    switch ((char) directionCharacter) {
                        case CHAR_A -> {
                           return new ArrowKey(utils.Direction.UP);
                        }
                        case CHAR_B -> {
                            return new ArrowKey(utils.Direction.DOWN);
                        }
                    }
                }
            }
        }

        return switch ((char) ch) {
            case CTRL_C -> ControlKey.CTRL_C;
            case TAB -> ControlKey.TAB;
            case ENTER, CARRIAGE -> ControlKey.ENTER;
            case BACKSPACE_CHAR, BACKSPACE_NUMERIC -> ControlKey.BACKSPACE;
            default -> new CharKey((char) ch);
        };
    }
}
