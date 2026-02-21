import commands.BaseCommand;
import commands.ExternalCommand;
import customErrors.CommandException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static utils.CommandHandlerUtils.getCommands;
import static utils.CommandHandlerUtils.parseInput;
import static utils.Constants.NEW_LINE;

public class NewCommandHandler implements BaseCommandHandler {

    AtomicReference<Exception> error = new AtomicReference<>();
    private OutputRedirectHandler outputRedirectHandler;

    private void processCommand(ArrayList<BaseCommand> commands) throws Exception {
        InputStream in = System.in;
        OutputStream errOut = outputRedirectHandler.getStdErrRedirect().getOutputStream();

        for (int index = 0; index < commands.size(); index++) {
            OutputStream out;

            if (index == (commands.size() - 1)) {
                out = outputRedirectHandler.getStdOutRedirect().getOutputStream();
                BaseCommand cmd = commands.get(index);
                try {
                    cmd.execute(in, out);
                } catch (Exception e) {
                    error.set(e);
                }
            }
            else {
                PipedOutputStream pos = new PipedOutputStream();
                PipedInputStream pis = new PipedInputStream(pos);

                out = pos;

                InputStream currentIn = in;

                BaseCommand cmd = commands.get(index);
                new Thread(() -> {
                    try {
                        cmd.execute(currentIn, out);
                    } catch (Exception e) {
                        error.set(e);
                    }

                }).start();

                in = pis;
            }
        }

        if (error.get() != null) {
            errOut.write(error.get().getMessage().getBytes());
            errOut.write(NEW_LINE.getBytes());
            errOut.flush();

            // Clear error after handling
            error.set(null);
        }
    }

    public void handleCommand(String input) throws Exception {
        ArrayList<String> arguments = parseInput(input);

        outputRedirectHandler = new OutputRedirectHandler(arguments);

        // Splits arguments by pipeline into multiple commands
        ArrayList<BaseCommand> commands = getCommands(arguments);

        // Handle actual execution of commands
        processCommand(commands);
    }
}