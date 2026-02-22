package utils;

import commands.BaseCommand;
import commands.ChangeDirectoryCommand;
import commands.EchoCommand;
import commands.ExitCommand;
import commands.HistoryCommand;
import commands.ListDirectoryCommand;
import commands.PwdCommand;
import commands.TypeCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class Constants {
    public static final char DOLLAR_SIGN = '$';
    public static final String NEW_LINE = "\r\n";
    public static final String BACKSPACE = "\b \b";
    public static final char BELL_CHAR = '\007';
    public static final char SINGLE_QUOTE = '\'';
    public static final char DOUBLE_QUOTE = '\"';
    public static final char BLANK_SPACE = ' ';
    public static final char BACKSLASH = '\\';
    public static final char BACKTICK = '`';
    public static final char ENTER = '\n';
    public static final char CARRIAGE = '\r';
    public static final char TAB = '\t';
    public static final char BACKSPACE_CHAR = '\b';
    public static final int BACKSPACE_NUMERIC = 127;
    public static final int CTRL_C = 3;
    public static final int ESCAPE = 27;
    public static final char BRACKET = '[';
    public static final char CHAR_A = 'A';
    public static final char CHAR_B = 'B';
    public static final String TILDE = "~";
    public static final String DOT = ".";
    public static final String DASH = "-";
    public static final String DOT_DOT = "..";
    public static final String FORWARD_SLASH = "/";
    public static final String GREATER_THAN = ">";
    public static final String REDIRECT_FILE_DESCRIPTOR_1 = "1>";
    public static final String REDIRECT_FILE_DESCRIPTOR_2 = "2>";
    public static final String APPEND_SYMBOL = ">>";
    public static final String APPEND_FILE_DESCRIPTOR_1 = "1>>";
    public static final String APPEND_FILE_DESCRIPTOR_2 = "2>>";
    public static final String USER_DIRECTORY = "user.dir";
    public static final String EMPTY = "";
    public static final Set<Character> DOUBLE_QUOTES_ESCAPABLE_CHARACTERS =
            Set.of(DOUBLE_QUOTE, BACKSLASH, DOLLAR_SIGN, BACKTICK);
    public static final Set<String> OVERWRITE_REDIRECTION_SYMBOLS =
            Set.of(GREATER_THAN, REDIRECT_FILE_DESCRIPTOR_1);
    public static final Set<String> APPEND_REDIRECTION_SYMBOLS =
            Set.of(APPEND_SYMBOL, APPEND_FILE_DESCRIPTOR_1);
    public static final Set<String> OVERWRITE_ERR_REDIRECTION_SYMBOLS =
            Set.of(REDIRECT_FILE_DESCRIPTOR_2);
    public static final Set<String> APPEND_ERR_REDIRECTION_SYMBOLS =
            Set.of(APPEND_FILE_DESCRIPTOR_2);
    public static final String PIPELINE_SYMBOL = "|";

    public static final Map<String, Function<ArrayList<String>, BaseCommand>> COMMANDS = Map.of(
            "echo", EchoCommand::new,
            "cd", ChangeDirectoryCommand::new,
            "pwd", PwdCommand::new,
            "type", TypeCommand::new,
            "exit", ExitCommand::new,
            "ls", ListDirectoryCommand::new,
            "history", HistoryCommand::new
    );
    public static final Set<String> BUILTIN_COMMANDS = COMMANDS.keySet();
}
