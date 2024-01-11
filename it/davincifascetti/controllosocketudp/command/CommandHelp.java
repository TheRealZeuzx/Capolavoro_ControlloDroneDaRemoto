package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.ServerThread;

/**CommandHelp permette di stampare il msg di help richiesto
 * 
 */
public class CommandHelp implements Command{
    private String msg;
    private ServerThread gestore = null;
    public CommandHelp(String msg){
        this.msg = msg;
    }
    public CommandHelp(String msg,ServerThread gestore){
        this(msg);
    }
    public void execute() {
        if(this.gestore == null)
            System.out.println(this.msg);
        else
            this.gestore.inviaMsg(this.msg);
    }
}
