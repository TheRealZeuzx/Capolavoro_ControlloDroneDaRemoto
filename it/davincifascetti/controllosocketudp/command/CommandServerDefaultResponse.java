
package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.ServerThread;

/**
    operazione di defualt che il serverThread svolge alla ricezione di un pacchetto dal client
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandServerDefaultResponse implements Command{
    private String msg;
    private ServerThread gestore = null;

    /**
        Costruttore di default di CommandServerDefaultResponse.
        Questo comando viene eseguito quando è necessario utilizzare l'operazione di defualt del server
        @param msg messaggio da stampare nel file
        @param gestore server thread che si occuperà di stampare nel file
    */
    public CommandServerDefaultResponse(ServerThread gestore,String msg) throws CommandException{
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
        try {
            gestore.defaultResponse(this.msg);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        new CommandHelp("operazione andata a buon fine",this.gestore).execute();
        
    }
}
