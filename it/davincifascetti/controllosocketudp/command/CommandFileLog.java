package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.ServerThread;

/**CommandHelp permette di stampare il msg di help richiesto
 * 
 */
public class CommandFileLog implements Command{
    private String msg;
    private ServerThread gestore = null;

    public CommandFileLog(String msg,ServerThread gestore) throws CommandException{
        this.msg = msg;
        this.gestore = gestore;
        if(gestore == null) throw new CommandException("errore, il gestore inserito è null");
    }
    public void execute() {
        gestore.fileLog(this.msg);
    }
}
