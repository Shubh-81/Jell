package commands;

public enum ShellBuiltins {
    CHANGE_DIRECTORY("cd"),
    ECHO("echo"),
    EXIT("exit"),
    PWD("pwd"),
    TYPE("type"),
    LIST_DIRECTORY("ls");


    private final String name;

    ShellBuiltins(String command) {
        name = command;
    }

    public String getName() {
        return name;
    }
}
