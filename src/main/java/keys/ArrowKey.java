package keys;

import utils.Direction;

public record ArrowKey(Direction direction) implements BaseKey {
    public String toString() {
        return "AroowKey: " + direction.toString();
    }
}
