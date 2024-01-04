
public interface Command {
    /**execute permette di eseguire il comando
     * 
     */
    public boolean execute();
    /**undo permette di annullare il comando
     * 
     */
    public void undo();
}
