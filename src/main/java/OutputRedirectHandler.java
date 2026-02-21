import customErrors.FileNotFoundError;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

import static utils.CommandHandlerUtils.writeToFile;
import static utils.Constants.APPEND_ERR_REDIRECTION_SYMBOLS;
import static utils.Constants.APPEND_REDIRECTION_SYMBOLS;
import static utils.Constants.OVERWRITE_ERR_REDIRECTION_SYMBOLS;
import static utils.Constants.OVERWRITE_REDIRECTION_SYMBOLS;

class OutputRedirect {
    // Flag denoting if output redirect is enabled
    private final boolean outputRedirect;
    // Stores path to which redirect output
    private final String outputRedirectPath;
    // Flag denoting if output is to be appended or overriden
    private final boolean appendMode;
    // Output stream to output to
    private final OutputStream outputStream;

    public OutputRedirect(boolean outputRedirect, String outputRedirectPath, boolean appendMode) throws FileNotFoundError {
        this.outputRedirect = outputRedirect;
        this.outputRedirectPath = outputRedirectPath;
        this.appendMode = appendMode;

        outputStream = buildOutputStream();
    }

    private OutputStream buildOutputStream() throws FileNotFoundError {
        // If no output redirection path is present simply use system.out
        if (!outputRedirect)    return System.out;

        try {
            return new FileOutputStream(outputRedirectPath, appendMode);
        } catch (Exception e) {
            throw new FileNotFoundError(outputRedirectPath);
        }
    }

    public boolean isOutputRedirect() {
        return outputRedirect;
    }

    public String getOutputRedirectPath() {
        return outputRedirectPath;
    }

    public boolean isAppendMode() {
        return appendMode;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

}

public class OutputRedirectHandler {
    private final OutputRedirect stdOutRedirect;
    private final OutputRedirect stdErrRedirect;

    public OutputRedirectHandler(ArrayList<String> arguments) throws FileNotFoundError {
        // Setup output redirect flags for stdout
        stdOutRedirect = buildOutputRedirection(
                arguments, OVERWRITE_REDIRECTION_SYMBOLS, APPEND_REDIRECTION_SYMBOLS);

        // Setup output redirect flags for stderr
        stdErrRedirect = buildOutputRedirection(
                arguments, OVERWRITE_ERR_REDIRECTION_SYMBOLS, APPEND_ERR_REDIRECTION_SYMBOLS);
    }

    // Checks if output redirection is present and sets path accordingly
    public OutputRedirect buildOutputRedirection(
            ArrayList<String> arguments,
            Set<String> overwriteRedirectionSymbols,
            Set<String> appendRedirectionSymbols
    ) throws FileNotFoundError {
        boolean outputRedirect = false;
        String outputRedirectionPath = null;
        boolean appendMode = false;

        for (int index = 0; index < arguments.size(); index++) {
            if (overwriteRedirectionSymbols.contains(arguments.get(index))) {
                appendMode = false;
            } else if (appendRedirectionSymbols.contains(arguments.get(index))) {
                appendMode = true;
            } else {
                continue;
            }

            if (index == (arguments.size() - 1)) {
                outputRedirect = false;
                outputRedirectionPath = null;
                break;
            }

            outputRedirect = true;
            outputRedirectionPath = arguments.get(index + 1);

            arguments.subList(index, arguments.size()).clear();

            // Do dummy write to path to create file
            writeToFile(outputRedirectionPath, "", appendMode);

            break;
        }

        return new OutputRedirect(outputRedirect, outputRedirectionPath, appendMode);
    }

    public OutputRedirect getStdOutRedirect() {
        return stdOutRedirect;
    }

    public OutputRedirect getStdErrRedirect() {
        return stdErrRedirect;
    }
}
