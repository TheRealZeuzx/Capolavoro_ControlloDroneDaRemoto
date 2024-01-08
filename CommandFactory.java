
public interface CommandFactory<T> {
    public Command getCommand(T gestore,String[] params)  throws CommandException;
}
