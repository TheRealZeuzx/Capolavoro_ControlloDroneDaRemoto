package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;

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
    public CommandStampaVideoServerThread(ServerThread gestore,String msg) throws CommandException{
        super(gestore, msg);
    }
    public void execute() throws CommandException, ErrorLogException {
        if(this.getParams().isBlank()) new CommandInviaMsgDefaultToClient(this.getGestore(),this.getParams()).execute();
        this.getGestore().stampaVideo("il client dice: " + this.getParams());
        new CommandInviaMsgToClient(this.getGestore(),"operazione andata a buon fine").execute();

    }

}
