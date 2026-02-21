package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import static utils.Constants.NEW_LINE;

public class ExternalCommand implements BaseCommand {
    private static ArrayList<String> args;
    private static String commandName;

    public ExternalCommand(String commandName, ArrayList<String> args) {
        this.args = args;
        this.commandName = commandName;
    }

    public String getName() {
        return commandName;
    }

    public void execute(InputStream in, OutputStream out) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(args);

        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.write((line + NEW_LINE).getBytes());
                out.flush();
            }
        }

        String errorOut = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                errorOut += line;
            }
        }

        if (!errorOut.isEmpty()) {
            throw new Exception(errorOut);
        }

        p.waitFor();
    }

    private static void transfer(InputStream in, OutputStream out) {
        try (in; out) {
            in.transferTo(out);
        } catch (IOException ignored) {}
    }
}
