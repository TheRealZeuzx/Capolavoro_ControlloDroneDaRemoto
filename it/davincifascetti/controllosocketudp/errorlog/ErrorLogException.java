package it.davincifascetti.controllosocketudp.errorlog;

/**Definisco con questa eccezione tutti gli errori che devono essere stampati sul file di testo
 * 
 */
public class ErrorLogException extends Exception{
    public ErrorLogException(String msg){
        super(msg);
    }
}
