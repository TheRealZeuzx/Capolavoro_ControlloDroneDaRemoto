package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;
import it.davincifascetti.controllosocketudp.program.Terminal;
import it.davincifascetti.controllosocketudp.program.Ui;

/**
 * GENERALE
    COMMAND FILE LOG. 
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandFileLog extends CommandI<ServerThread>{

    /**
        Costruttore di default di CommandFileLog.
        Questo comando viene eseguito quando è necessario stampare in un file specificato un messaggio. il messaggio viene stampato anche sulla console del server
        @param msg messaggio da stampare nel file
        @param gestore server thread che si occuperà di stampare nel file
    */
    public CommandFileLog(ServerThread gestore,String msg,Ui ui) throws CommandException{
        super(gestore,msg,ui);
    }

    /**
        Si occupa di chiamare le funzioni corrispondenti al fine del comando.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
     * @throws ErrorLogException 
     */
    public void execute() throws CommandException, ErrorLogException {
        try {
            ((Terminal)this.getUi()).getGestoreRisposte().add(this.getGestore().getServer()).fileLog(getParams(), this.getGestore().getServer());
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        new CommandStampaVideoServerThread(this.getGestore(),this.getParams(),this.getUi()).execute();
        new CommandInviaMsgToClient(this.getGestore(),"operazione andata a buon fine",this.getUi()).execute();
        
    }
}
