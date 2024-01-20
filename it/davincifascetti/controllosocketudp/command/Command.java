package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

/**
    Interfaccia generale Command SENZA undo.
    Interfaccia generale che devono implementare tutti i comandi.
    Necessaria per l'implemento del Command Design Pattern.
    @author Tommaso Mussaldi, Bonfiglio Mattia
    @version 1.0
 */
public interface Command {

    /**
        Execute: Metodo in cui verranno chiamate tutte le funzioni relative al comando.
        @throws CommandException se un comando riscontra un errore
     */
    public void execute() throws CommandException, ErrorLogException;
    
}
