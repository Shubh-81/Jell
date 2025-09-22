import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandHandler {
    private final HashMap<String, Consumer<String[]>> commands = new HashMap<>();
    private final String[] paths;

    public CommandHandler(String path) {
        paths = path.split(":");
        commands.put("echo", this::handleEcho);
        commands.put("exit", this::handleExit);
        commands.put("type", this::handleType);
        commands.put("pwd", this::handlePwd);
        commands.put("cd", this::handleCd);
    }

    private void handlePwd(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }

    private void handleCd(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException("Invalid command, expected: cd <dir>");
        }

        String currPath = System.getProperty("user.dir");

        List<String> finalPath = new ArrayList<>(Arrays.asList(currPath.split("/")));
        List<String> argPath =  new ArrayList<>(Arrays.asList(args[1].split("/")));

        for (String curr: argPath) {
            curr = curr.trim();
            if (curr.equals(".")) {
                continue;
            } else if (curr.equals("..")) {
                if (!finalPath.isEmpty()) {
                    finalPath.remove(finalPath.size() - 1);
                }
            } else {
                finalPath.add(curr);
            }
        }

        String res = String.join("/", finalPath);
        if (res.length() == 0) {
            res = "/";
        }

        File file = new File(res);
        if (file.exists() && file.isDirectory()) {
            System.setProperty("user.dir", file.getAbsolutePath());
        } else {
            System.out.println("cd: " + res + ": No such file or directory");
        }
    }

    private void handleEcho(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Invalid command, expected: echo <args>");
        }

        for (int i = 1; i < args.length; i++) {
            System.out.print(args[i] + " ");
        }

        System.out.println();
    }

    private void handleExit(String[] args) {
        System.exit(0);
    }

    private void handleType(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Invalid command, expected: type <args>");
        }

        String command = args[1];

        if (commands.containsKey((command))) {
            System.out.println(command + " is a shell builtin");
        } else {
            boolean found = false;

            for (String path: paths) {
                if (path.startsWith("$")) {
                    break;
                }

                String fullPath = path + "/" + command;
                File file = new File(fullPath);
                
                if (file.exists() && file.canExecute()) {
                    System.out.println(command + " is " + fullPath);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println(command + ": not found");
            }
        }
    }

    private void handleDefault(String[] args) {
        String command = args[0];
        boolean found = false;

        for (String path: paths) {
            if (path.startsWith("$")) {
                break;
            }

            String fullPath = path + "/" + command;
            File file = new File(fullPath);

            if (file.exists() && file.canExecute()) {
                found = true;
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(args);
                    processBuilder.directory(new File(path));
                    Process process = processBuilder.start();
                    
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Error while executing command: " + e.getMessage());
                }
                break;
            }
        }

        if (!found) {
            System.out.println(command + ": not found");
        }
    }

    public void handleCommand(String input) {
        String[] args = input.trim().split(" ");
        commands.getOrDefault(args[0], this::handleDefault).accept(args);
    }
}
