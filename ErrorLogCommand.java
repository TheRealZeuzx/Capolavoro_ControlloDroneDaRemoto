import java.io.IOException;

/**permette di eseguire 
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
