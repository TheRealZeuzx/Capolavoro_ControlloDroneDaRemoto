package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;
import it.davincifascetti.controllosocketudp.program.Ui;

/** stampa a video usando Server.stampaVideo(String) così da avere i vantaggi di quel metodo (vedi Server.stampaVideo)
 *  invia al client risposta di successo
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandStampaVideoServerThread extends CommandI<ServerThread>{

    /**
     * 
     * @param msg messaggio da stampare 
     * @param gestore thread di risposta che si occuperà di stampare e inviare msg risposta
     * @throws CommandException
     */
    public CommandStampaVideoServerThread(ServerThread gestore,String msg,Ui ui) throws CommandException{
        super(gestore, msg,ui);
    }
    public void execute() throws CommandException, ErrorLogException {
        if(this.getParams().isBlank()) new CommandInviaMsgDefaultToClient(this.getGestore(),this.getParams(),this.getUi()).execute();
        new CommandInviaMsgToClient(this.getGestore(),"operazione andata a buon fine",this.getUi()).execute();

    }

}
