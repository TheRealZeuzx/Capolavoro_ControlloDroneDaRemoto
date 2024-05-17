package it.davincifascetti.controllosocketudp.command;

/**
 * GENERALE
    Eccezione personalizzata sollevata da classi che implementano Commandable.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
 */
public class CommandableException extends Exception{
    /**
        Costruttore di default di CommandableException.
        @param msg Messaggio da restituire come errore
    */
    public CommandableException(String msg){
        super(msg);
    }
}
