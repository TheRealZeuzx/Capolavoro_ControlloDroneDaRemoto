
public interface Command {
    /**execute permette di eseguire il comando
     * 
     */
    public void execute();
    /**undo permette di annullare il comando
     * 
     */
    public void undo();
}
