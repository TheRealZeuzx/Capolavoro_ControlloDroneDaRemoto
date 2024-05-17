package it.davincifascetti.controllosocketudp.command;

/**
 * SERVER
    CommandDisattivaServer. 
    Eccezione personalizzata sollevata da un qualsiasi comando.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandException extends Exception{
    /**
        Costruttore di default di CommandException.
        @param msg messaggio d'errore. 
    */
    public CommandException(String msg){
        super(msg);
    }
}
