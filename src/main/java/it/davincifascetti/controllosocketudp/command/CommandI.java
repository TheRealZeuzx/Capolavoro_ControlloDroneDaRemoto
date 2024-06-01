package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Ui;

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
    private Ui ui = null;
    
    public CommandI(T gestore,String params,Ui ui) throws CommandException{
        if(gestore == null) throw new CommandException("Errore, hai inserito un gestore null");
        this.gestore = gestore;
        if(params == null) throw new CommandException("Errore, non ci sono parametri");
        this.params = params;
        if(ui == null) throw new CommandException("Errore, Ui è null!");
        this.ui = ui;
    }
    /**
        getGestore().
        Ritorna il gestore del comando.
        @return Il gestore del comando.
    */
    public T getGestore(){return this.gestore;}
    public String getParams(){return this.params;}
    public Ui getUi(){return this.ui;}
}
