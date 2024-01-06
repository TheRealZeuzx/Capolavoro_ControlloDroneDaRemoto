import java.util.Stack;

public class CommandHistory {
    private Stack<UndoableCommand> history = new Stack<>();

    public void push(UndoableCommand c) {
        history.push(c);
    }

    public UndoableCommand pop() {
        return history.pop();
    }

    public boolean isEmpty() { return history.isEmpty(); }
}