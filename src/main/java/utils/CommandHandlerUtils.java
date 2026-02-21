package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static utils.Constants.BACKSLASH;
import static utils.Constants.BLANK_SPACE;
import static utils.Constants.BUILTIN_COMMANDS;
import static utils.Constants.COMMANDS;
import static utils.Constants.DOUBLE_QUOTE;
import static utils.Constants.DOUBLE_QUOTES_ESCAPABLE_CHARACTERS;
import static utils.Constants.NEW_LINE;
import static utils.Constants.PIPELINE_SYMBOL;
import static utils.Constants.SINGLE_QUOTE;

import commands.BaseCommand;
import commands.ExternalCommand;
import commands.PipelineCommand;
import customErrors.CommandNotFoundError;
import customErrors.FileNotFoundError;
import utils.SystemProperties;

public class CommandHandlerUtils {

    // Parses input string into list of command and argument
    public static ArrayList<String> parseInput(String input) throws IllegalArgumentException {
        ArrayList<String> arguments = new ArrayList<>();

        // Tracks whether inside quotes or not
        boolean singleQuotesOpen = false;
        boolean doubleQuotesOpen = false;

        // Tracks current argument
        StringBuilder argument = new StringBuilder();

        for (int index = 0; index < input.length(); index++) {
            // Extract current character
            char ch = input.charAt(index);

            switch (ch) {
                // Single Quote
                case SINGLE_QUOTE: {
                    if (!doubleQuotesOpen)
                        singleQuotesOpen = !singleQuotesOpen;
                    else
                        argument.append(ch);
                    break;
                }
                // Double Quote
                case DOUBLE_QUOTE: {
                    if (!singleQuotesOpen)
                        doubleQuotesOpen = !doubleQuotesOpen;
                    else
                        argument.append(ch);
                    break;
                }
                // Empty Space
                case BLANK_SPACE: {
                    boolean quotesOpen = singleQuotesOpen || doubleQuotesOpen;

                    // If quotes are open, just add space to argument
                    if (quotesOpen) {
                        argument.append(ch);
                        break;
                    }

                    // If argument is non empty, add it to arguments
                    if (!argument.isEmpty())
                        arguments.add(argument.toString());

                    // Reset argument
                    argument.setLength(0);

                    break;
                }
                // Escape Character
                case BACKSLASH: {
                    // Inside single quote, backslash is treated as regular character
                    if (singleQuotesOpen) {
                        argument.append(ch);
                        break;
                    }

                    // If there is no character after backslash it's simply added
                    if (index == (input.length() - 1)) {
                        argument.append(ch);
                        break;
                    }

                    char nextChar = input.charAt(index + 1);
                    // If double quotes are open and character is not in double quotes escapable characters, do nothing
                    if (doubleQuotesOpen && !DOUBLE_QUOTES_ESCAPABLE_CHARACTERS.contains(nextChar)) {
                        argument.append(ch);
                        break;
                    }

                    // Add next character to string
                    argument.append(nextChar);
                    // Skip next index
                    index += 1;

                    break;
                }
                default: {
                    // By default simply add character to current argument
                    argument.append(ch);
                }
            }
        }

        // If quotes are not closed, throw error
        if (doubleQuotesOpen || singleQuotesOpen) {
            throw new IllegalArgumentException(NEW_LINE + "Unclosed quotes");
        }

        // If argument is non empty add it to argument
        if (!argument.isEmpty()) {
            arguments.add(argument.toString());
        }

        return arguments;
    }

    // Writes to a file
    public static void writeToFile(String outputPath, String output, boolean appendMode) throws FileNotFoundError {
        try {
            FileWriter fw = new FileWriter(outputPath, appendMode);
            fw.write(output);
            fw.close();
        } catch (IOException e) {
            throw new FileNotFoundError(outputPath);
        }
    }

    // Replaces command name by absolute path
    private static ArrayList<BaseCommand> replaceCommandByPath(ArrayList<ArrayList<String>> commands) throws CommandNotFoundError {
        ArrayList<BaseCommand> mappedCommands = new ArrayList<>();

        for (ArrayList<String> command: commands) {
            String commandName = command.get(0);

            if (BUILTIN_COMMANDS.contains(commandName)) {
                Function<ArrayList<String>, BaseCommand> factory = COMMANDS.get(commandName);

                BaseCommand currentCommand = factory.apply(command);
                mappedCommands.add(currentCommand);
            }
            else if (SystemProperties.getExecutables().containsKey(commandName)) {
                // Replace command name by absolute path
//                command.set(0, SystemProperties.getExecutables().get(commandName));

                ExternalCommand currentCommand = new ExternalCommand(commandName, command);
                mappedCommands.add(currentCommand);
            } else {
                // If command is not in executables and not in builtin commands throw error
                throw new CommandNotFoundError(commandName);
            }
        }

        return mappedCommands;
    }

    // Current runs pipeline command directly by process builder
    private static BaseCommand buildPipelineCommand(ArrayList<String> arguments) {
        String commandString = String.join(" ", arguments);
        return new PipelineCommand(commandString);
    }


    // Converts list of arguments into list of Base Command, however currently only one command will be returned.
    public static ArrayList<BaseCommand> getCommands(ArrayList<String> arguments) throws CommandNotFoundError {
        // Temporary override for pipeline command
        if (arguments.contains(PIPELINE_SYMBOL)) {
            return new ArrayList<>(List.of(buildPipelineCommand(arguments)));
        }

        ArrayList<ArrayList<String>> commands = new ArrayList<>();
        int N = arguments.size();

        // Stores arguments for current command
        ArrayList<String> currentCommands = new ArrayList<>();

        for (int index = 0; index <= N; index++) {
            if (index == N) {
                if (!currentCommands.isEmpty())
                    // Here new list is required as otherwise same object will be added
                    commands.add(new ArrayList<>(currentCommands));
                currentCommands.clear();
            } else {
                currentCommands.add(arguments.get(index));
            }
        }

        return replaceCommandByPath(commands);
    }

}
