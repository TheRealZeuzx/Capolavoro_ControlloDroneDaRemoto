
public interface CommandFactory<T> {
    
    public abstract Command getCommand(T gestore,String[] params)  throws CommandException;
}
