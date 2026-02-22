package commands;

import customErrors.CommandException;
import customErrors.FileNotFoundError;
import customErrors.InvalidArguments;
import history.HistoryHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

    private void updateHistory(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            HistoryHandler.addCommand(line);
        }
    }

    private void writeHistory(File file, int startIndex, boolean append) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, append));
        ArrayList<String> commands = HistoryHandler.getPreviousCommands();

        for (int index = startIndex; index < commands.size(); index++) {
            bw.write(commands.get(index));
            bw.newLine();
        }

        bw.flush();
    }

    private boolean readHistoryFromFile() throws Exception {
        if (args.size() > 2) {
            String filePath = args.get(2);

            File file = new File(filePath);
            if (!file.exists() || file.isDirectory()) {
                throw new CommandException(getName(), new FileNotFoundError(filePath));
            }

            updateHistory(file);
            return true;
        }

        return false;
    }

    private boolean writeHistoryToFile() throws Exception {
        if (args.size() > 2) {
            String filePath = args.get(2);

            File file = new File(filePath);

            if (file.isDirectory()) {
                throw new CommandException(getName(), new InvalidArguments());
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            writeHistory(file, 0,false);
            return true;
        }

        return false;
    }

    private boolean appendHistoryToFile() throws Exception {
        if (args.size() > 2) {
            String filePath = args.get(2);

            File file = new File(filePath);

            if (!file.exists() || file.isDirectory()) {
                throw new CommandException(getName(), new FileNotFoundError(filePath));
            }

            writeHistory(file, HistoryHandler.getAppendIndex(),true);
            HistoryHandler.updatedAppendIndex();
            return true;
        }

        return false;
    }

    public void execute(InputStream in, OutputStream out) throws Exception {
        int maxHistory = -1;
        if (args.size() > 1) {
            switch (args.get(1)) {
                case "-r" -> {
                    if (readHistoryFromFile()) {
                        return;
                    }
                }
                case "-w" -> {
                    if (writeHistoryToFile()) {
                        return;
                    }
                }
                case "-a" -> {
                    if (appendHistoryToFile()) {
                        return;
                    }
                }
                default -> {
                    try {
                        maxHistory = Integer.parseInt(args.get(1));
                    } catch (Exception e) {
                        throw new CommandException(getName(), new InvalidArguments());
                    }
                }
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
