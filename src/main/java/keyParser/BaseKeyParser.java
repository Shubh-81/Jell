package keyParser;

import keys.BaseKey;

public interface BaseKeyParser {
    BaseKey readKey() throws Exception;
}
