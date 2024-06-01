
package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;
import it.davincifascetti.controllosocketudp.program.Terminal;
import it.davincifascetti.controllosocketudp.program.Ui;

/**
    operazione di defualt che il serverThread svolge alla ricezione di un pacchetto dal client
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandServerDefaultResponse extends CommandI<ServerThread>{

    /**
        Costruttore di default di CommandServerDefaultResponse.
        Questo comando viene eseguito quando è necessario utilizzare l'operazione di defualt del server
        @param msg messaggio da stampare nel file
        @param gestore server thread che si occuperà di stampare nel file
    */
    public CommandServerDefaultResponse(ServerThread gestore,String msg,Ui ui) throws CommandException{
        super(gestore, msg,ui);
    }

    /**
        Si occupa di chiamare le funzioni corrispondenti al fine del comando.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
     * @throws ErrorLogException 
     */
    public void execute() throws CommandException, ErrorLogException {
        try {
            ((Terminal)this.getUi()).getGestoreRisposte().defaultResponse(this.getParams(),this.getGestore().getServer());
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        new CommandInviaMsgToClient(this.getGestore(),"operazione andata a buon fine",this.getUi()).execute();
        
    }
}
