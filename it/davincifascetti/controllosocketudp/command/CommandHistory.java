package it.davincifascetti.controllosocketudp.command;

import java.util.Stack;

/** 
    CommandHistory. 
    Permette di salvare in uno stack i comandi che implementano UndoableCommand, 
    e che quindi dispongono del metodo undo.
    @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandHistory {
    private Stack<UndoableCommand> history = new Stack<>();

    /**
        push.
        Comando per inserire nello stack un nuovo undoable command.
        @param c Comando da inserire
    */
    public void push(UndoableCommand c) {
        history.push(c);
    }

    /**
        pop.
        Comando per rimuovere dallo stack l'ultimo undoable command.
    */
    public UndoableCommand pop() {
        return history.pop();
    }

    /**
        isEmpty()
        @return boolean true se lo stack è vuoto, altrimenti falso. 
    */
    public boolean isEmpty() { return history.isEmpty(); }
}