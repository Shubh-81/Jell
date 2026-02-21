package customErrors;

public class CommandNotFoundError extends Exception {
    String command;

    public CommandNotFoundError(String command) {
        this.command = command;
    }

    @Override
    public String getMessage() {
        return command + ": command not found";
    }

}
