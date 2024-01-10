package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

/**interfaccia Command senza undo
 * 
 */
public interface Command {

    /**execute permette di eseguire il comando
     * @throws CommandException nel caso in cui ci sia un errore , viene sollevato CommandException contentente il msg di errore
     */
    public void execute() throws CommandException, ErrorLogException;
    
}
