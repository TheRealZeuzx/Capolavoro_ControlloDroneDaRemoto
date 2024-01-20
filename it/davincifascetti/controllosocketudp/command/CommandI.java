package it.davincifascetti.controllosocketudp.command;

/** 
    Abstract CommandI. 
    Permette di avere i comandi di tipo T sotto l'interfaccia Command.
    @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public abstract class CommandI<T extends Commandable> implements Command{
    private T gestore;
    /**
        Costruttore generale di CommandI
        @param gestore è l'oggetto che farà da receiver per i comandi
        @throws CommandException in caso di errori
     */
    public CommandI(T gestore) throws CommandException{
        if(gestore == null) throw new CommandException("Errore, hai inserito un gestore null");
        this.gestore = gestore;
    }
    /**
        getGestore().
        Ritorna il gestore del comando.
        @return Il gestore del comando.
    */
    public T getGestore(){return this.gestore;}
}
