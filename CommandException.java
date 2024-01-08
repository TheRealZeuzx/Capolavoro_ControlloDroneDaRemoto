/**CommandException , tutte le eccezioni sollevate dall esecuzione dei comandi e dagli errori nella creazione
 * 
 */
public class CommandException extends Exception{
    public CommandException(String msg){
        super(msg);
    }
}
