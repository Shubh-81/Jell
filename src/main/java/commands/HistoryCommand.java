package commands;

import customErrors.CommandException;
import customErrors.InvalidArguments;
import history.HistoryHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static commands.ShellBuiltins.HISTORY;
import static utils.Constants.NEW_LINE;

public class HistoryCommand implements BaseCommand {

    private final List<String> args;

    public HistoryCommand(List<String> args) {
        this.args = args;
    }

    public String getName() {
        return HISTORY.getName();
    }

    public void execute(InputStream in, OutputStream out) throws Exception {
        int maxHistory = -1;
        if (args.size() > 1) {
            try {
                maxHistory = Integer.parseInt(args.get(1));
            } catch (Exception e) {
                throw new CommandException(getName(), new InvalidArguments());
            }
        }

        List<String> historicalCommands = HistoryHandler.getPreviousCommands();
        int numCommands = historicalCommands.size();

        int start = 0;
        if (maxHistory > 0 && maxHistory < numCommands) {
            start = numCommands - maxHistory;
        }

        for (int index = start; index < numCommands; index++) {
            String output = index + " " + historicalCommands.get(index);
            out.write(output.getBytes());
            out.write(NEW_LINE.getBytes());
        }
        out.flush();
    }

}
