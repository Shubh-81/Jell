package commands;

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
        List<String> historicalCommands = HistoryHandler.getPreviousCommands();

        for (int index = 0; index < historicalCommands.size(); index++) {
            String output = index + " " + historicalCommands.get(index);
            out.write(output.getBytes());
            out.write(NEW_LINE.getBytes());
        }
        out.flush();
    }

}
