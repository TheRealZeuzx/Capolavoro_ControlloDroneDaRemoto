package it.davincifascetti.controllosocketudp.command;

/**questa classe astratta permette di creare un comando che necessita di un gestore (che sia Client Server o GestoreClientServer)
 * creando un costruttore comune
 */
public abstract class CommandI<T extends Commandable> implements Command{
    private T gestore;
    public CommandI(T gestore) throws CommandException{
        if(gestore == null) throw new CommandException("Errore, hai inserito un gestore null");
        this.gestore = gestore;
    }
    public T getGestore(){return this.gestore;}
}
