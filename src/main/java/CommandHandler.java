//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.function.Consumer;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class CommandHandler implements BaseCommandHandler {
//    private final HashMap<String, Consumer<String[]>> commands = new HashMap<>();
//    private final String[] paths;
//    private final String[] homePath;
//    private Boolean outputRedirect;
//    private Boolean errorRedirect;
//    private String outputRedirectionPath;
//    private Boolean firstOutput;
//    private ArrayList<ArrayList<String>> nextCommands;
//    private HashMap<String, String> executables;
//
//    public CommandHandler(String path, String homePath, HashMap<String, String> executables) {
//        paths = path.split(":");
//        this.homePath = homePath.split("/");
//        this.outputRedirect = false;
//        this.outputRedirectionPath = "";
//        this.firstOutput = true;
//        this.executables = executables;
//
//        commands.put("echo", this::handleEcho);
//        commands.put("exit", this::handleExit);
//        commands.put("type", this::handleType);
//        commands.put("pwd", this::handlePwd);
//        commands.put("cd", this::handleCd);
//    }
//
//    private void handleOutput(String output) {
//        if (!outputRedirect) {
//            System.out.print(output);
//            return;
//        }
//
//        try {
//            FileWriter fw = new FileWriter(outputRedirectionPath, !firstOutput);
//            firstOutput = false;
//            fw.write(output + "\n");
//            fw.close();
//        } catch (IOException e) {
//            System.out.println("\rError while writing output: " + e.getMessage());
//        }
//    }
//
//    private void handleErrorOutput(String output) {
//        if (!errorRedirect) {
//            System.out.print(output + "\r\n");
//            return;
//        }
//
//        try {
//            FileWriter fw = new FileWriter(outputRedirectionPath, !firstOutput);
//            firstOutput = false;
//            fw.write(output + "\n");
//            fw.close();
//        } catch (IOException e) {
//            System.out.println("\rError while writing output: " + e.getMessage());
//        }
//    }
//
//    private void handlePwd(String[] args) {
//        handleOutput(System.getProperty("user.dir" + "\n\r"));
//    }
//
//    private void handleCd(String[] args) {
//        if (args.length != 2) {
//            throw new RuntimeException("Invalid command, expected: cd <dir>");
//        }
//
//        String currPath = System.getProperty("user.dir");
//
//        List<String> finalPath = new ArrayList<>(Arrays.asList(currPath.split("/")));
//        List<String> argPath =  new ArrayList<>(Arrays.asList(args[1].split("/")));
//
//        if (!argPath.isEmpty() && (argPath.get(0).length() == 0 || argPath.get(0).equals("~"))) {
//            finalPath.clear();
//        }
//
//        for (String curr: argPath) {
//            curr = curr.trim();
//            if (curr.equals(".") || curr.equals("")) {
//                continue;
//            } else if (curr.equals("..")) {
//                if (!finalPath.isEmpty()) {
//                    finalPath.remove(finalPath.size() - 1);
//                }
//            } else if (curr.equals("~")) {
//                for (String path: homePath) {
//                    path = path.trim();
//                    if (path.length() == 0) continue;
//                    finalPath.add(path);
//                }
//            } else {
//                finalPath.add(curr);
//            }
//        }
//
//        String res = String.join("/", finalPath);
//        res = "/" + res;
//
//        File file = new File(res);
//        if (file.exists() && file.isDirectory()) {
//            System.setProperty("user.dir", file.getAbsolutePath());
//        } else {
//            handleOutput("cd: " + res + ": No such file or directory\n\r");
//        }
//    }
//
//    private void handleEcho(String[] args) {
//        if (args.length < 2) {
//            throw new RuntimeException("Invalid command, expected: echo <args>");
//        }
//
//        String res = "";
//        for (int i = 1; i < args.length; i++) {
//            res += args[i] + " ";
//        }
//
//        handleOutput(res + "\n\r");
//    }
//
//    private void handleExit(String[] args) {
//        try {
//            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty cooked echo < /dev/tty"});
//        } catch (IOException e) {
//            System.out.println("\rFaced IOException: " + e.getMessage());
//        }
//        System.exit(0);
//    }
//
//    private void handleType(String[] args) {
//        if (args.length < 2) {
//            throw new RuntimeException("Invalid command, expected: type <args>");
//        }
//
//        String command = args[1];
//
//        if (commands.containsKey((command))) {
//            handleOutput(command + " is a shell builtin" + "\n\r");
//        } else {
//            boolean found = false;
//
//            for (String path: paths) {
//                if (path.startsWith("$")) {
//                    break;
//                }
//
//                String fullPath = path + "/" + command;
//                File file = new File(fullPath);
//
//                if (file.exists() && file.canExecute()) {
//                    handleOutput(command + " is " + fullPath + "\n\r");
//                    found = true;
//                    break;
//                }
//            }
//
//            if (!found) {
//                handleOutput(command + ": not found" + "\n\r");
//            }
//        }
//    }
//
//    private void handleDefault(String[] _args) {
//        try {
//            ArrayList<ProcessBuilder> processeBuilders = new ArrayList<>();
//            boolean wc = false;
//
//            for (ArrayList<String> currArgs: nextCommands) {
//                String command = currArgs.get(0);
//                if (command.trim().equals("wc"))    wc = true;
//                if (!executables.containsKey(command)) {
//                    handleOutput(command + ": not found\n\r");
//                    return;
//                }
//
//                currArgs.set(0, executables.get(command));
//                ProcessBuilder pb = new ProcessBuilder(currArgs);
//                processeBuilders.add(pb);
//            }
//
//            boolean finalWc = wc;
//            ArrayList<Thread> outputThreads = new ArrayList<>();
//
//            List<Process> processes = ProcessBuilder.startPipeline(processeBuilders);
//
//            for (int i = 1; i < processes.size(); i++) {
//                Process currProcess = processes.get(i);
//                Process prevProcess = processes.get(i - 1);
//                Thread pipe = new Thread(() -> {
//                    try {
//                        OutputStream currIn = currProcess.getOutputStream();
//                        InputStream prevOut = prevProcess.getInputStream();
//                        int byteRead;
//
//                        while ((byteRead = prevOut.read()) != -1) {
//                            try {
//                                currIn.write(byteRead);
//                                currIn.flush();
//                            } catch (IOException e) {
//                                break;
//                            }
//                        }
//                    } catch (IOException e) {
//                        prevProcess.destroy();
//                    }
//                });
//
//                Thread errorPipe = new Thread(() -> {
//                    try {
//                        InputStream errorIn = prevProcess.getErrorStream();
//                        OutputStream errorOut = System.out;
//
//                        int byteRead;
//                        while ((byteRead = errorIn.read()) != -1) {
//                            errorOut.write(byteRead);
//                            errorOut.flush();
//                        }
//                    } catch (IOException e) {
//                        System.out.println("\n\rError in process: " + e.getMessage());
//                    }
//                });
//
//                pipe.start();
//                errorPipe.start();
//                outputThreads.add(pipe);
//                outputThreads.add(errorPipe);
//            }
//
//            if (processes.size() > 0) {
//                Thread outputStream = new Thread(() -> {
//                    try {
//                        Process last = processes.getLast();
//                        InputStream in = last.getInputStream();
//                        OutputStream out = System.out;
//
//                        int byteRead;
//                        if (finalWc) out.write(32);
//                        while ((byteRead = in.read()) != -1) {
//                            out.write(byteRead);
//                            if (byteRead == '\n')   out.write('\r');
//                            out.flush();
//                        }
//                        System.out.print("\r");
//                    } catch (IOException e) {
//                        System.out.println("\rError while executing process: " + e.getMessage());
//                    }
//                });
//
//                Thread errorOutputStream = new Thread(() -> {
//                    try {
//                        Process last = processes.getLast();
//
//                        int byteRead;
//                        InputStream errorIn = last.getErrorStream();
//                        OutputStream errorOut = System.out;
//
//                        while ((byteRead = errorIn.read()) != -1) {
//                            errorOut.write(byteRead);
//                            errorOut.flush();
//                        }
//                    } catch (IOException e) {
//                        System.out.println("\rError while executing process: " + e.getMessage());
//                    }
//                });
//
//                outputStream.start();
//                errorOutputStream.start();
//                outputThreads.add(outputStream);
//                outputThreads.add(errorOutputStream);
//            }
//
//            for (Process process: processes) {
//                process.waitFor();
//            }
//
//            for (Thread currThread: outputThreads) {
//                currThread.join();
//            }
//        } catch (IOException | InterruptedException e) {
//            System.out.println("\n\r" + e.getMessage());
//        }
//    }
//
//    public void handleCommand(String input) {
//        ArrayList<String> args = new ArrayList<>();
//        boolean isOpen1 = false;
//        boolean isOpen2 = false;
//        String curr = "";
//
//        outputRedirect = false;
//        errorRedirect = false;
//        outputRedirectionPath = "";
//        firstOutput = true;
//
//        nextCommands = new ArrayList<>();
//
//        for (int i = 0; i < input.length(); i++) {
//            char ch = input.charAt(i);
//            if (ch == '\'' && !isOpen2) {
//                isOpen1 = !isOpen1;
//            } else if (ch == '\"' && !isOpen1) {
//                isOpen2 = !isOpen2;
//            } else if (ch == ' ' && !isOpen1 && !isOpen2) {
//                if (!curr.isEmpty())  args.add(curr);
//                curr = "";
//            } else if (ch == '\\' && !isOpen1) {
//                if (i == (input.length() - 1)) {
//                    curr += '\\';
//                    continue;
//                }
//                char ch2 = input.charAt(i + 1);
//                if (isOpen2 && ch2 != '\"' && ch2 != '\\' && ch2 != '$' && ch2 != '`') {
//                    curr += '\\';
//                    continue;
//                }
//                curr += ch2;
//                i += 1;
//            } else {
//                curr += ch;
//            }
//        }
//
//        if (!curr.isEmpty()) {
//            args.add(curr);
//        }
//
//        if (args.contains(">") || args.contains("1>")) {
//            outputRedirect = true;
//            int idx = args.contains(">") ? args.indexOf(">"): args.indexOf("1>");
//
//            if (idx < (args.size() - 1)) {
//                outputRedirectionPath = args.get(idx + 1);
//                try {
//                    FileWriter fw = new FileWriter(outputRedirectionPath, !firstOutput);
//                    fw.write("");
//                    fw.close();
//                } catch (IOException e) {
//                    System.out.println("\rError while creating redirection file " + e.getMessage());
//                }
//                args = new ArrayList<>(args.subList(0, idx));
//            } else {
//                outputRedirect = false;
//            }
//        }
//
//        if (args.contains(">>") || args.contains("1>>")) {
//            outputRedirect = true;
//            firstOutput = false;
//            int idx = args.contains(">>") ? args.indexOf(">>"): args.indexOf("1>>");
//
//            if (idx < (args.size() - 1)) {
//                outputRedirectionPath = args.get(idx + 1);
//                try {
//                    FileWriter fw = new FileWriter(outputRedirectionPath, !firstOutput);
//                    fw.write("");
//                    fw.close();
//                } catch (IOException e) {
//                    System.out.println("\rError while creating redirection file " + e.getMessage());
//                }
//                args = new ArrayList<>(args.subList(0, idx));
//            } else {
//                outputRedirect = false;
//            }
//        }
//
//        if (args.contains("2>")) {
//            errorRedirect = true;
//            int idx = args.indexOf("2>");
//
//            if (idx < (args.size() - 1)) {
//                outputRedirectionPath = args.get(idx + 1);
//                try {
//                    FileWriter fw = new FileWriter(outputRedirectionPath, !firstOutput);
//                    fw.write("");
//                    fw.close();
//                } catch (IOException e) {
//                    System.out.println("\rError while creating redirection file " + e.getMessage());
//                }
//                args = new ArrayList<>(args.subList(0, idx));
//            } else {
//                errorRedirect = false;
//            }
//        }
//
//        if (args.contains("2>>")) {
//            errorRedirect = true;
//            firstOutput = false;
//            int idx = args.indexOf("2>>");
//
//            if (idx < (args.size() - 1)) {
//                outputRedirectionPath = args.get(idx + 1);
//                try {
//                    FileWriter fw = new FileWriter(outputRedirectionPath, !firstOutput);
//                    fw.write("");
//                    fw.close();
//                } catch (IOException e) {
//                    System.out.println("Error while creating redirection file " + e.getMessage());
//                }
//                args = new ArrayList<>(args.subList(0, idx));
//            } else {
//                errorRedirect = false;
//            }
//        }
//
//        ArrayList<String> currArgs = new ArrayList<>();
//
//        for (int i = 0; i <= args.size(); i++) {
//            if (i == args.size() || args.get(i).equals("|")) {
//                if (!currArgs.isEmpty())    nextCommands.add(new ArrayList<>(currArgs));
//                currArgs.clear();
//            } else  currArgs.add(args.get(i));
//        }
//
//        commands.getOrDefault(args.get(0), this::handleDefault).accept(args.toArray(new String[0]));
//    }
//}
