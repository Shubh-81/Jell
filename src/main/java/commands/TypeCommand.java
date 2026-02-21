package commands;

import customErrors.CommandNotFoundError;
import customErrors.TypeCommandNotFound;
import utils.SystemProperties;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static utils.Constants.BUILTIN_COMMANDS;
import static utils.Constants.NEW_LINE;

public class TypeCommand implements BaseCommand {
    ArrayList<String> args;

    public TypeCommand(ArrayList<String> args) {
        this.args = args;
    }

    public String getName() {
        return ShellBuiltins.TYPE.getName();
    }

    public void execute(InputStream in, OutputStream out) throws Exception {
        if (args.size() < 2) {
            return;
        }

        String queryCommand = args.get(1);

        if (BUILTIN_COMMANDS.contains(queryCommand)) {
            out.write((queryCommand + " is a shell builtin").getBytes());
            out.write(NEW_LINE.getBytes());
            out.flush();

            return;
        }

        if (!SystemProperties.getExecutables().containsKey(queryCommand)) {
            throw new TypeCommandNotFound(queryCommand);
        }

        String executablePath = SystemProperties.getExecutables().get(queryCommand);
        out.write((queryCommand + " is " + executablePath).getBytes());
        out.write(NEW_LINE.getBytes());
        out.flush();
    }
}
