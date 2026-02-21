package commands;

import customErrors.CommandException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static utils.Constants.NEW_LINE;

public class PwdCommand implements BaseCommand {

    ArrayList<String> args;

    public PwdCommand(ArrayList<String> args) {
        this.args = args;
    }

    public String getName() {
        return ShellBuiltins.PWD.getName();
    }

    public void execute(InputStream in, OutputStream out) throws Exception {
        if (args.size() > 1) {
            throw new CommandException(getName(), new Exception("too many arguments"));
        }

        String currentDirectory = System.getProperty("user.dir");
        out.write(currentDirectory.getBytes());
        out.write(NEW_LINE.getBytes());
        out.flush();
    }
}
