package customErrors;

public class TypeCommandNotFound extends Exception {
    String command;

    public TypeCommandNotFound(String command) {
        this.command = command;
    }

    @Override
    public String getMessage() {
        return command + " not found";
    }
}
