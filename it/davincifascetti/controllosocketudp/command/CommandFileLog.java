package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.ServerThread;

/**CommandHelp permette di stampare il msg di help richiesto
 * 
 */
public class CommandFileLog implements Command{
    private String msg;
    private ServerThread gestore = null;

    public CommandFileLog(String msg,ServerThread gestore){
        this.msg = msg;
        this.gestore = gestore;
    }
    public void execute() {
        if(gestore == null)
            throw new NullPointerException("gestore is null");
        gestore.fileLog(this.msg);
    }
}
