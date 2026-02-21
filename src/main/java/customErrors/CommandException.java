package customErrors;

public class CommandException extends Exception {
    final Exception exceptionFaced;
    final String commandName;

    public CommandException(String commandName, Exception exceptionFaced) {
        this.exceptionFaced = exceptionFaced;
        this.commandName = commandName;
    }

    @Override
    public String getMessage() {
        return commandName + ": " + exceptionFaced.getMessage();
    }

}
