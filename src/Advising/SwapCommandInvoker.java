package Advising;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Command invoker that manages command execution and history
 */
public class SwapCommandInvoker {
    private final Stack<SwapCommand> executedCommands;
    private final Stack<SwapCommand> undoneCommands;
    
    public SwapCommandInvoker() {
        this.executedCommands = new Stack<>();
        this.undoneCommands = new Stack<>();
    }
    
    /**
     * Executes a command and adds it to history
     * @param command The command to execute
     * @return SwapResult from command execution
     */
    public SwapResult executeCommand(SwapCommand command) {
        SwapResult result = command.execute();
        executedCommands.push(command);
        undoneCommands.clear(); // Clear redo history when new command is executed
        return result;
    }
    
    /**
     * Undoes the last executed command
     * @return SwapResult from undo operation, null if no commands to undo
     */
    public SwapResult undoLastCommand() {
        if (executedCommands.isEmpty()) {
            return null;
        }
        
        SwapCommand command = executedCommands.pop();
        SwapResult result = command.undo();
        undoneCommands.push(command);
        return result;
    }
    
    /**
     * Redoes the last undone command
     * @return SwapResult from redo operation, null if no commands to redo
     */
    public SwapResult redoLastCommand() {
        if (undoneCommands.isEmpty()) {
            return null;
        }
        
        SwapCommand command = undoneCommands.pop();
        SwapResult result = command.execute();
        executedCommands.push(command);
        return result;
    }
    
    /**
     * Gets history of executed commands
     * @return List of command descriptions
     */
    public List<String> getCommandHistory() {
        List<String> history = new ArrayList<>();
        for (SwapCommand command : executedCommands) {
            history.add(command.getDescription());
        }
        return history;
    }
    
    /**
     * Clears all command history
     */
    public void clearHistory() {
        executedCommands.clear();
        undoneCommands.clear();
    }
}