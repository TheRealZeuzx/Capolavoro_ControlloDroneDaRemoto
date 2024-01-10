/**Interfaccia per la generalizzazione delle varie factory
 * 
 */
public interface CommandFactory {
    
    public abstract Command getCommand(String[] params)  throws CommandException;
}
