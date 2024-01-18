package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Server;

/** stampa la lista di messaggi ricevuti dal client
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandStampaMsgRicevuti extends CommandI<Server>{

    /**
     * 
     * @param gestore Server di cui stampare la lista di msg ricevuti
     * @throws CommandException
     */
    public CommandStampaMsgRicevuti(Server gestore) throws CommandException {
        super(gestore);
    }

    @Override
    public void execute() throws CommandException, ErrorLogException {
        System.out.println(this.getGestore().stampaStoriaMsg().equals("") ? "" : "\n\nMessaggi Ricevuti: \n" + this.getGestore().stampaStoriaMsg());//stampo la storia di msg ricevuti dal client

    }
    
}
