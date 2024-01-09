public abstract class CommandFactoryI<T extends Commandable> {
    private T gestore;
    public CommandFactoryI(T gestore) throws CommandException{
        if(gestore == null) throw new CommandException("Errore, hai inserito un gestore null");
        this.gestore = gestore;
    }
    public T getGestore(){return this.gestore;}
}
