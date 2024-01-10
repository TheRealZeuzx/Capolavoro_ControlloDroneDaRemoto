package it.davincifascetti.controllosocketudp.command;

import java.util.Stack;
/**Permette di salvare in uno stack i comandi che implementano UndoableCommand , che quindi dispongono del metodo  undo
 * 
 */
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