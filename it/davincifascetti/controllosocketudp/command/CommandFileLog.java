package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.ServerThread;

/**
    CommandHelp. 
    Permette di stampare a video il messaggio di help richiesto
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandFileLog implements Command{
    private String msg;
    private ServerThread gestore = null;

    /**
        Costruttore di default di CommandFileLog.
        Questo comando viene eseguito quando è necessario stampare in un file specificato un messaggio.
        @param msg messaggio da stampare nel file
        @param gestore server thread che si occuperà di stampare nel file
    */
    public CommandFileLog(String msg,ServerThread gestore) throws CommandException{
        this.msg = msg;
        this.gestore = gestore;
        if(msg == null) throw new CommandException("Errore, il msg inserito risulta essere null");
        if(gestore == null) throw new CommandException("errore, il gestore inserito è null");
    }

    /**
        Si occupa di chiamare le funzioni corrispondenti al fine del comando.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
     */
    public void execute() throws CommandException {
        gestore.fileLog(this.msg);
        new CommandHelp("operazione andata a buon fine",this.gestore).execute();
        
    }
}
