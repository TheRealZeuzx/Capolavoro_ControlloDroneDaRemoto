package it.davincifascetti.controllosocketudp.command;

/**classe CommandFactory<T extends Commandable>
 * serve per accumunare tutti i commandFactory il cui gestore  estende Commandable
 * 
 */
public abstract class CommandFactoryI<T extends Commandable> {
    private T gestore;
    /**costruttore di gestore
     * 
     * @param gestore deve implementare commandable, è l'oggetto che farà da receiver per i comandi
     * @throws CommandException
     */
    public CommandFactoryI(T gestore) throws CommandException{
        if(gestore == null) throw new CommandException("Errore, hai inserito un gestore null");
        this.gestore = gestore;
    }
    public T getGestore(){return this.gestore;}
}
