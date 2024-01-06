/**interfaccia Command con metodo undo
 * 
 */
public interface UndoableCommand extends Command{

    /**undo permette di annullare il comando
     * 
     */
    public void undo();
} 
