









# Jell - Advanced Java Shell Implementation

A sophisticated POSIX-compliant shell implementation in Java, built for the CodeCrafters "Build Your Own Shell" challenge. This implementation showcases advanced software engineering patterns including dependency injection, modular architecture, and comprehensive shell features.

## Features

- **Interactive REPL** with advanced keyboard handling
- **Command History** with persistent storage and navigation (↑/↓ arrows)
- **Tab Completion** using Trie data structure for efficient prefix matching
- **Built-in Commands**: `cd`, `pwd`, `echo`, `ls`, `type`, `exit`, [history](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java/history:0:0-0:0)
- **External Command Execution** with proper process management
- **Output Redirection** (`>`, `>>`, `1>`, `1>>`, `2>`, `2>>`)
- **Pipeline Support** (`|`) for command chaining
- **Error Handling** with custom exception hierarchy
- **Dependency Injection** using Google Guice

## Architecture

### Design Patterns

- **Dependency Injection**: Google Guice manages all component dependencies
- **Strategy Pattern**: Different command types implement [BaseCommand](cci:2://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/BaseCommand.java:6:0-10:1) interface
- **Singleton Pattern**: History handler uses `@Singleton` for state management
- **Factory Pattern**: Command factory in `Constants.COMMANDS` map
- **Observer Pattern**: Input handling with key press events

### Core Components

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   NewMain.java  │───▶│  ShellModule.java │───▶│  Guice Injector │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │ InputHandler.java│
                       └──────────────────┘
                                │
                ┌───────────────┼───────────────┐
                ▼               ▼               ▼
        ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
        │ Command     │ │ History     │ │ AutoComplete│
        │ Handler     │ │ Handler     │ │ Handler     │
        └─────────────┘ └─────────────┘ └─────────────┘
```

## Package Structure

### [/src/main/java/](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java:0:0-0:0)

#### **Core Handlers**
- [NewMain.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/NewMain.java:0:0-0:0) - Application entry point with Guice setup
- [InputHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/InputHandler.java:0:0-0:0) - Main REPL loop and keyboard event handling
- [NewCommandHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/NewCommandHandler.java:0:0-0:0) - Command parsing and execution orchestration
- [AutoCompleteHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/AutoCompleteHandler.java:0:0-0:0) - Tab completion with Trie-based matching
- [OutputRedirectHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/OutputRedirectHandler.java:0:0-0:0) - Output redirection and pipeline management

#### **[commands/](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands:0:0-0:0) Package**
- [BaseCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/BaseCommand.java:0:0-0:0) - Interface for all command implementations
- [EchoCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/EchoCommand.java:0:0-0:0) - Built-in echo command
- [ChangeDirectoryCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/ChangeDirectoryCommand.java:0:0-0:0) - Directory navigation
- [PwdCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/PwdCommand.java:0:0-0:0) - Print working directory
- [ListDirectoryCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/ListDirectoryCommand.java:0:0-0:0) - Directory listing
- [TypeCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/TypeCommand.java:0:0-0:0) - Command type identification
- [ExitCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/ExitCommand.java:0:0-0:0) - Shell termination
- [HistoryCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/HistoryCommand.java:0:0-0:0) - Command history management
- [ExternalCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/ExternalCommand.java:0:0-0:0) - External program execution
- [PipelineCommand.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/commands/PipelineCommand.java:0:0-0:0) - Pipeline command support

#### **[history/](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java/history:0:0-0:0) Package**
- `BaseHistoryHandler.java` - Interface for history management
- [HistoryHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/history/HistoryHandler.java:0:0-0:0) - Persistent command history with file I/O

#### **[keyParser/](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java/keyParser:0:0-0:0) Package**
- `BaseKeyParser.java` - Interface for keyboard input parsing
- `KeyParser.java` - Raw input to key object conversion

#### **[keys/](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java/keys:0:0-0:0) Package**
- `BaseKey.java` - Base class for all key types
- `CharKey.java` - Regular character keys
- `ArrowKey.java` - Arrow key with direction
- `ControlKey.java` - Special keys (Ctrl+C, Tab, Enter, Backspace)

#### **[DataStructures/](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java/DataStructures:0:0-0:0) Package**
- [Trie.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/DataStructures/Trie.java:0:0-0:0) - Prefix tree for efficient autocomplete
- [TrieNode.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/DataStructures/TrieNode.java:0:0-0:0) - Node implementation for Trie

#### **[utils/](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java/utils:0:0-0:0) Package**
- [Constants.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/utils/Constants.java:0:0-0:0) - Application constants and command registry
- [CommandHandlerUtils.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/utils/CommandHandlerUtils.java:0:0-0:0) - Command parsing and processing utilities
- [SystemProperties.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/utils/SystemProperties.java:0:0-0:0) - System property accessors
- [StartupShutdownHandlers.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/utils/StartupShutdownHandlers.java:0:0-0:0) - Application lifecycle hooks
- [ExecutableProvider.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/utils/ExecutableProvider.java:0:0-0:0) - System executable discovery
- [SystemExecutableProvider.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/utils/SystemExecutableProvider.java:0:0-0:0) - Implementation of executable discovery
- [Direction.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/utils/Direction.java:0:0-0:0) - Enum for arrow key directions

#### **[customErrors/](cci:9://file:///Users/shubh.agarwal/Code/Jell/src/main/java/customErrors:0:0-0:0) Package**
- [CommandException.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/customErrors/CommandException.java:0:0-0:0) - Base command exception
- [CommandNotFoundError.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/customErrors/CommandNotFoundError.java:0:0-0:0) - Command not found error
- [FileNotFoundError.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/customErrors/FileNotFoundError.java:0:0-0:0) - File operation errors
- [InvalidArguments.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/customErrors/InvalidArguments.java:0:0-0:0) - Invalid argument errors
- [TypeCommandNotFound.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/customErrors/TypeCommandNotFound.java:0:0-0:0) - Type command specific errors

## Key Classes & Responsibilities

### [NewMain.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/NewMain.java:0:0-0:0)
- Application entry point
- Initializes Guice dependency injection container
- Manages application startup and shutdown lifecycle

### [InputHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/InputHandler.java:0:0-0:0)
- Core REPL (Read-Eval-Print Loop) implementation
- Handles keyboard input parsing and event dispatch
- Manages command history navigation
- Coordinates with autocomplete and command handlers

### [NewCommandHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/NewCommandHandler.java:0:0-0:0)
- Parses command input into tokens
- Handles command pipeline creation
- Manages output redirection setup
- Executes commands with proper I/O stream management

### [HistoryHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/history/HistoryHandler.java:0:0-0:0)
- Maintains persistent command history
- Implements history navigation (↑/↓ arrows)
- Handles history file I/O operations
- Manages history state and indexing

### [AutoCompleteHandler.java](cci:7://file:///Users/shubh.agarwal/Code/Jell/src/main/java/AutoCompleteHandler.java:0:0-0:0)
- Implements tab completion functionality
- Uses Trie data structure for efficient prefix matching
- Integrates system executables with built-in commands
- Provides intelligent completion suggestions

## Build & Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Jell
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the shell**
   ```bash
   ./your_program.sh
   ```

### Manual Build & Execution

1. **Compile and package**
   ```bash
   mvn clean package -DargLine="-XX:+EnableDynamicAgentLoading" -DargLine="-Xlog:disable" -Ddir=/tmp/codecrafters-build-shell-java
   ```

2. **Run the JAR**
   ```bash
   java -jar /tmp/codecrafters-build-shell-java/codecrafters-shell.jar
   ```

### CodeCrafters Submission

1. **Commit changes**
   ```bash
   git commit -am "implement shell features"
   ```

2. **Push to submit**
   ```bash
   git push origin master
   ```

## 🎮 Usage Examples

### Basic Commands
```bash
$ echo "Hello, World!"
Hello, World!

$ pwd
/Users/shubh.agarwal/Code/Jell
```

### History Navigation
```bash
$ echo "first command"
first command
$ echo "second command"  
second command
$ [Press ↑]  # Shows "echo second command"
$ [Press ↑]  # Shows "echo first command"
$ [Press ↓]  # Shows "echo second command"
```

### Tab Completion
```bash
$ ec[TAB]     # Completes to "echo"
$ ls[TAB]     # Shows available executables starting with "ls"
```

### Output Redirection
```bash
$ echo "output" > file.txt
$ echo "append" >> file.txt
$ command 2> error.log
$ command 2>> error.log
```

### Pipeline
```bash
$ ls -la | grep ".java"
$ echo "hello" | wc -c
```

## Testing

Run the test suite:
```bash
mvn test
```

## Dependencies

- **Google Guice** (7.0.0) - Dependency injection framework
- **SLF4J** (2.0.13) - Logging framework
- **Lombok** (1.18.32) - Code generation annotations
- **JUnit Jupiter** (5.10.2) - Unit testing framework
- **Mockito** (4.11.0) - Mocking framework for tests

## License

This project is part of the CodeCrafters challenge and follows the challenge's licensing terms.




