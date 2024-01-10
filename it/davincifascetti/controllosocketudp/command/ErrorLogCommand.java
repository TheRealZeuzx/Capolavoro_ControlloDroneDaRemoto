package it.davincifascetti.controllosocketudp.command;

import java.io.IOException;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
//TODO cosa implementa? command o qualcos'altro?
/**permette di stampare in modalità append il msg di errore
 * 
 */
public class ErrorLogCommand{

    private ErrorLog Elog;
    private String msg;
    public ErrorLogCommand(ErrorLog Elog,String msg){

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
