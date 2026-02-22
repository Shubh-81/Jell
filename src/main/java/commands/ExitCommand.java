package commands;

import utils.StartupShutdownHandlers;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ExitCommand implements BaseCommand {
    ArrayList<String> args;

    public ExitCommand(ArrayList<String> args) {
        this.args = args;
    }

    public String getName() {
        return ShellBuiltins.EXIT.getName();
    }

    public void execute(InputStream in, OutputStream out) throws Exception {
        StartupShutdownHandlers.onShutDown();

        // Exit program
        System.exit(0);
    }
}
