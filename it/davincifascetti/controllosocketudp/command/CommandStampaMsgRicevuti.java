package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Server;

public class CommandStampaMsgRicevuti extends CommandI<Server>{

    public CommandStampaMsgRicevuti(Server gestore) throws CommandException {
        super(gestore);
    }

    @Override
    public void execute() throws CommandException, ErrorLogException {
        System.out.println(this.getGestore().stampaStoriaMsg().equals("") ? "" : "\n\nMessaggi Ricevuti: \n" + this.getGestore().stampaStoriaMsg());//stampo la storia di msg ricevuti dal client

    }
    
}
