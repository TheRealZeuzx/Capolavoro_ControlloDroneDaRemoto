package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

/** 
 * INTERFACE
    Abstract CommandI. 
    Permette di avere i comandi di tipo T sotto l'interfaccia Command.
    @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public abstract class CommandI<T extends Commandable> implements Command{
    private T gestore = null;
    private String params = null;

    
    public CommandI(T gestore,String params) throws CommandException{
        if(gestore == null) throw new CommandException("Errore, hai inserito un gestore null");
        this.gestore = gestore;
        if(params == null) throw new CommandException("Errore, non ci sono parametri");
        this.params = params;
    }
    /**
        getGestore().
        Ritorna il gestore del comando.
        @return Il gestore del comando.
    */
    public T getGestore(){return this.gestore;}
    public String getParams(){return this.params;}
}
