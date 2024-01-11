package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

/**interfaccia Command con metodo undo
 * 
 */
public interface UndoableCommand extends Command{

    /**undo permette di annullare il comando
     * 
     */
    public boolean undo() throws CommandException,ErrorLogException;
} 
