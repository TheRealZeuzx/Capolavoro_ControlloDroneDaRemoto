/**interfaccia Command senza undo
 * 
 */
public interface Command {

    /**execute permette di eseguire il comando
     * 
     */
    public void execute() throws CommandException;
    
}
