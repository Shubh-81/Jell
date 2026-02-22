package utils;

import java.util.HashMap;

public class SystemExecutableProvider implements ExecutableProvider {

    @Override
    public HashMap<String, String> getExecutables() {
        return SystemProperties.getExecutables();
    }
}
