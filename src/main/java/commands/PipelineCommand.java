package commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static utils.Constants.BLANK_SPACE;
import static utils.Constants.NEW_LINE;

public class PipelineCommand implements BaseCommand {

    private final String command;

    public PipelineCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return command.split(String.valueOf(BLANK_SPACE))[0];
    }

    @Override
    public void execute(InputStream in, OutputStream out) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("sh", "-c", command);
        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.write((line + NEW_LINE).getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        }

        p.waitFor();
    }
}
