package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;

/** 
    CommandInviaMsgClient. 
    Permette l'invio di un messaggio da parte di un client.
    @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandInviaMsgClient extends CommandI<Client>{
    private String msg;

    /** 
        Costruttore di CommandInviaMsgClient
        @param msg Messaggio da inviare
        @param gestore Gestore da cui inviare il messaggio
    */
    public CommandInviaMsgClient(Client gestore,String msg) throws CommandException {
        super(gestore,msg);
        this.msg = msg;
    }

    /**
        Permette l'invio di un messaggio da parte di un client. chiama il metodo inviaMsg di client
    */
    @Override
    public void execute() throws CommandException, ErrorLogException {
        try {
            this.getGestore().inviaMsg(this.msg);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }

}
