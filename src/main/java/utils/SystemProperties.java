package utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static utils.Constants.FORWARD_SLASH;

public class SystemProperties {

    public static final HashMap<String, String> executables;
    public static final String path;
    public static final String historyFilePath;
    public static final List<String> homePath;
    public static final String currentPath;

    static {
        historyFilePath = System.getenv("HISTFILE");
        path = System.getenv("PATH");
        homePath = List.of(System.getenv("HOME").split(FORWARD_SLASH));
        currentPath = System.getProperty("user.dir");

        executables = populateExecutables();
    }

    // Reads all paths in PATH and populates executables by all executables in the PATH
    private static HashMap<String, String> populateExecutables() {
        HashMap<String, String> executables = new HashMap<>();

        String[] paths = path.split(":");
        for (String currPath: paths) {
            File dir = new File(currPath);
            File[] files = dir.listFiles();

            if (files == null)  continue;

            for (File file: files) {
                if (file != null && file.isFile() && file.canExecute())
                    executables.put(file.getName(), file.getAbsolutePath());
            }
        }

        return executables;
    }

    public static HashMap<String, String> getExecutables() {
        return executables;
    }

    public static List<String> getHomePath() {
        return homePath;
    }

    public static String getHistoryPath() { return  historyFilePath; }
}
