package commands;

import customErrors.CommandException;
import customErrors.FileNotFoundError;
import utils.SystemProperties;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.Constants.EMPTY;
import static utils.Constants.DOT;
import static utils.Constants.DOT_DOT;
import static utils.Constants.FORWARD_SLASH;
import static utils.Constants.TILDE;
import static utils.Constants.USER_DIRECTORY;

public class ChangeDirectoryCommand implements BaseCommand {
    ArrayList<String> args;

    public ChangeDirectoryCommand(ArrayList<String> args) {
        this.args = args;
    }

    public String getName() {
        return ShellBuiltins.CHANGE_DIRECTORY.getName();
    }

    public void execute(InputStream in, OutputStream out) throws Exception {
        // Navigate to users home directory in case
        if (args.size() < 2) {
            args = new ArrayList<>(List.of(args.get(0), "~"));
        }

        // Paths from argument
        List<String> argPaths = new ArrayList<>(Arrays.asList(args.get(1).split(FORWARD_SLASH)));

        // To store complete path, initialized with current directory
        List<String> path = new ArrayList<>(Arrays.asList(System.getProperty(USER_DIRECTORY).split(FORWARD_SLASH)));

        // If first entry in argPaths is empty, it represents argument of format \ indicating absolute path
        // If first entry equals ~, it represents move to user's root directory
        if (!argPaths.isEmpty() &&
                (argPaths.get(0).isEmpty() || argPaths.get(0).equals(TILDE))) {
            path.clear();
        }

        for (String curr: argPaths) {
            curr = curr.trim();

            switch (curr) {
                // Place-holder for current working directory, no action needed
                case DOT:
                case EMPTY:
                    break;
                // Move up a directory
                case DOT_DOT: {
                    if (!path.isEmpty()) {
                        // Remove last path from path
                        path.remove(path.size() - 1);
                    }

                    break;
                }
                // Move to user's root directory
                case TILDE: {
                    for (String currPath: SystemProperties.getHomePath()) {
                        currPath = currPath.trim();
                        if (!currPath.isEmpty())
                            path.add(currPath);
                    }

                    break;
                }
                // By default append current to existing path
                default: {
                    path.add(curr);
                }
            }
        }

        String joinedPath = String.join("/", path);
        joinedPath = FORWARD_SLASH + joinedPath;

        File file = new File(joinedPath);
        if (file.exists() && file.isDirectory())
            System.setProperty(USER_DIRECTORY, file.getAbsolutePath());
        else
            throw new CommandException(getName(), new FileNotFoundError(joinedPath));
    }
}
