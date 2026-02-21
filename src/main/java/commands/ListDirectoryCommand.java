package commands;

import customErrors.CommandException;
import customErrors.FileNotFoundError;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static utils.Constants.DASH;
import static utils.Constants.DOT;
import static utils.Constants.NEW_LINE;
import static utils.Constants.TILDE;


enum ListDirectoryFlag {
    LONG_LISTING("-l"),
    SHOW_HIDDEN_FILES("-a"),
    SORT_BY_TIME("-t"),
    REVERSE("-r"),
    NEW_LINE("-1");


    private final String rawFlag;

    ListDirectoryFlag(String rawFlag) {
        this.rawFlag = rawFlag;
    }

    public String getRawFlag() {
        return rawFlag;
    }
}

public class ListDirectoryCommand implements BaseCommand {
    private final ArrayList<String> args;
    private static final String SPACE = "          ";

    public ListDirectoryCommand(ArrayList<String> args) {
        this.args = args;
    }

    public String getName() {
        return ShellBuiltins.LIST_DIRECTORY.getName();
    }

    public void execute(InputStream in, OutputStream out) throws Exception {

        boolean long_listing = false;
        boolean hidden_files = false;
        boolean time_sorted = false;
        boolean reverse = false;

        String seperator = SPACE;

        int reqFileCount = -1;

        List<String> paths = null;

        for (int idx = 1; idx < args.size(); idx++) {
            String currentArg = args.get(idx);
            if (!currentArg.startsWith(DASH)) {
                paths = args.subList(idx, args.size());
                break;
            }

            if (currentArg.equals(ListDirectoryFlag.LONG_LISTING.getRawFlag())) {
                long_listing = true;
            } else if (currentArg.equals(ListDirectoryFlag.SHOW_HIDDEN_FILES.getRawFlag())) {
                hidden_files = true;
            } else if (currentArg.equals(ListDirectoryFlag.SORT_BY_TIME.getRawFlag())) {
                time_sorted = true;
            } else if (currentArg.equals(ListDirectoryFlag.REVERSE.getRawFlag())) {
                reverse = true;
            } else if (currentArg.equals(ListDirectoryFlag.NEW_LINE.getRawFlag())) {
                seperator = NEW_LINE;
            }
        }

        if (paths == null || paths.isEmpty()) {
            paths = List.of(DOT);
        }

        for (String path: paths) {
            File dir = new File(path);

            // If directory does not exists, raise error
            if (!dir.exists()) {
                throw new CommandException(getName(), new FileNotFoundError(path));
            }

            // If directory is file just return file name
            if (dir.isFile() || (dir.listFiles() == null)) {
                out.write(path.getBytes());
                out.write(NEW_LINE.getBytes());
                out.flush();

                continue;
            }

            List<File> files = new ArrayList<>(Arrays.asList(dir.listFiles()));
            files.sort(Comparator.comparing(File::getName));

            String result = "";
            for (File file: files) {
                result += file.getName();
                result += seperator;
            }

            result = result.trim();
            out.write(result.getBytes());
            out.write(NEW_LINE.getBytes());
            out.flush();
        }
    }
}
