import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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
    private final String[] homePath;
    private Boolean outputRedirect;
    private String outputRedirectionPath;
    private Boolean firstOutput;

    public CommandHandler(String path, String homePath) {
        paths = path.split(":");
        this.homePath = homePath.split("/");
        this.outputRedirect = false;
        this.outputRedirectionPath = "";
        this.firstOutput = true;

        commands.put("echo", this::handleEcho);
        commands.put("exit", this::handleExit);
        commands.put("type", this::handleType);
        commands.put("pwd", this::handlePwd);
        commands.put("cd", this::handleCd);
    }

    private void handleOutput(String output) {
        if (!outputRedirect) {
            System.out.println(output);
            return;
        }

        try {
            FileWriter fw = new FileWriter(outputRedirectionPath + "\n", !firstOutput);
            firstOutput = false;
            fw.write(output);
            fw.close();
        } catch (IOException e) {
            System.out.println("Error while writing output: " + e.getMessage());
        }
    }

    private void handlePwd(String[] args) {
        handleOutput(System.getProperty("user.dir"));
    }

    private void handleCd(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException("Invalid command, expected: cd <dir>");
        }

        String currPath = System.getProperty("user.dir");

        List<String> finalPath = new ArrayList<>(Arrays.asList(currPath.split("/")));
        List<String> argPath =  new ArrayList<>(Arrays.asList(args[1].split("/")));

        if (!argPath.isEmpty() && (argPath.get(0).length() == 0 || argPath.get(0).equals("~"))) {
            finalPath.clear();
        }

        for (String curr: argPath) {
            curr = curr.trim();
            if (curr.equals(".") || curr.equals("")) {
                continue;
            } else if (curr.equals("..")) {
                if (!finalPath.isEmpty()) {
                    finalPath.remove(finalPath.size() - 1);
                }
            } else if (curr.equals("~")) {
                for (String path: homePath) {
                    path = path.trim();
                    if (path.length() == 0) continue;
                    finalPath.add(path);
                }
            } else {
                finalPath.add(curr);
            }
        }

        String res = String.join("/", finalPath);
        res = "/" + res;

        File file = new File(res);
        if (file.exists() && file.isDirectory()) {
            System.setProperty("user.dir", file.getAbsolutePath());
        } else {
            handleOutput("cd: " + res + ": No such file or directory");
        }
    }

    private void handleEcho(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Invalid command, expected: echo <args>");
        }

        String res = "";
        for (int i = 1; i < args.length; i++) {
            res += args[i] + " ";
        }

        handleOutput(res);
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
            handleOutput(command + " is a shell builtin");
        } else {
            boolean found = false;

            for (String path: paths) {
                if (path.startsWith("$")) {
                    break;
                }

                String fullPath = path + "/" + command;
                File file = new File(fullPath);
                
                if (file.exists() && file.canExecute()) {
                    handleOutput(command + " is " + fullPath);
                    found = true;
                    break;
                }
            }

            if (!found) {
                handleOutput(command + ": not found");
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
                        handleOutput(line);
                    }
                } catch (IOException e) {
                    System.out.println("Error while executing command: " + e.getMessage());
                }
                break;
            }
        }

        if (!found) {
            handleOutput(command + ": not found");
        }
    }

    public void handleCommand(String input) {
        ArrayList<String> args = new ArrayList<>();
        boolean isOpen1 = false;
        boolean isOpen2 = false;
        String curr = "";

        outputRedirect = false;
        outputRedirectionPath = "";
        firstOutput = true;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == '\'' && !isOpen2) {
                isOpen1 = !isOpen1;
            } else if (ch == '\"' && !isOpen1) {
                isOpen2 = !isOpen2;
            } else if (ch == ' ' && !isOpen1 && !isOpen2) {
                if (curr.length() > 0)  args.add(curr);
                curr = "";
            } else if (ch == '\\' && !isOpen1) {
                if (i == (input.length() - 1)) {
                    curr += '\\';
                    continue;
                }
                char ch2 = input.charAt(i + 1);
                if (isOpen2 && ch2 != '\"' && ch2 != '\\' && ch2 != '$' && ch2 != '`') {
                    curr += '\\';
                    continue;
                }
                curr += ch2;
                i += 1;
            } else {
                curr += ch;
            }
        }

        if (curr.length() > 0) {
            args.add(curr);
        }

        if (args.contains(">") || args.contains("1>")) {
            outputRedirect = true;
            int idx = args.contains(">") ? args.indexOf(">"): args.indexOf("1>");

            if (idx < (args.size() - 1)) {
                outputRedirectionPath = args.get(idx + 1);
                args = new ArrayList<>(args.subList(0, idx));
            } else {
                outputRedirect = false;
            }
        }

        commands.getOrDefault(args.get(0), this::handleDefault).accept(args.toArray(new String[0]));
    }
}
