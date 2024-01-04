import java.io.IOException;

/**permette di eseguire 
 * Nota: 
 * non è un command convenzionale perchè non ha un undo, quindi non avendo undo non implementa interfaccia Command altrimenti potrei usarlo in
 * CommandHistory ma causerebbe problemi dato che non ha undo...
 * 
 */
public class ErrorLogCommand{

    private Error Elog;
    private String msg;
    public ErrorLogCommand(Error Elog,String msg){

        this.Elog = Elog;
        this.msg = msg;
    }
    public boolean execute(){
        try {
            Elog.appendToStream(msg);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    

}
