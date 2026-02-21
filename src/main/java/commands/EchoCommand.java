package commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static utils.Constants.NEW_LINE;

public class EchoCommand implements BaseCommand {

    ArrayList<String> args;

    public EchoCommand(ArrayList<String> args) {
        this.args = args;
    }

    public String getName() {
        return ShellBuiltins.ECHO.getName();
    }

    public void execute(InputStream in, OutputStream out) throws Exception {
        if (args.size() > 1) {
            String output = String.join(" ", args.subList(1, args.size()));
            out.write(output.getBytes());
        }
        out.write(NEW_LINE.getBytes());
        out.flush();
    }
}
