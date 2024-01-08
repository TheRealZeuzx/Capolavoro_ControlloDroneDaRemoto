/**Interfaccia per la generalizzazione delle varie factory
 * 
 */
public interface CommandFactory {
    
    public abstract Command getCommand(GestoreClientServer gestore,String[] params)  throws CommandException;
    public abstract Command getCommand(Server gestore,String[] params)  throws CommandException;
    public abstract Command getCommand(Client gestore,String[] params)  throws CommandException;
}
