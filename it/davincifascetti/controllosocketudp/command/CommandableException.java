package it.davincifascetti.controllosocketudp.command;

/**
    Eccezione personalizzata sollevata da classi che implementano Commandable.
    @author Tommaso Mussaldi, Mattia Bonfiglio
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
