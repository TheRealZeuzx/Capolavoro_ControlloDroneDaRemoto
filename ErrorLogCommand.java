import java.io.IOException;
//TODO cosa implementa? command o qualcos'altro?
/**permette di stampare in modalità append il msg di errore
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
