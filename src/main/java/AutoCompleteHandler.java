public class AutoCompleteHandler {

    private final String[] builtinCommands = {"exit", "echo"};

    public String autoComplete(String input) {
        String match = "";

        for (String command: builtinCommands) {
            if (command.startsWith(input)) {
                if (match.length() != 0) {
                    System.out.print('\007');
                    return "";
                }
                match = command.substring(input.length());
            }
        }

        if (match.length() == 0) {
            System.out.print('\007');
        }

        return match;
    }
}
