package it.davincifascetti.controllosocketudp.command;

import java.io.IOException;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
/**permette di stampare in modalità append il msg di errore; non implementa command perchè il suo execute restituisce un bool
 * @author Mattia Bonfiglio
 * @version 1.0
 */
public class ErrorLogCommand{

    private ErrorLog Elog;
    private String msg;
    /**
     * 
     * @param Elog oggetto error log che fara da receiver
     * @param msg messaggio di errore
     */
    public ErrorLogCommand(ErrorLog Elog,String msg){
        this.Elog = Elog;
        this.msg = msg;
    }
    public boolean execute(){
        try {
            Elog.log(msg);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    

}
