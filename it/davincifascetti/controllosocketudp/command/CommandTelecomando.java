package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;

/** permette di avviare la modalità telecomando di un client
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandTelecomando extends CommandI<Client>{
    /**
     * 
     * @param gestore client receiver
     * @throws CommandException
     */
    public CommandTelecomando(Client gestore,String params) throws CommandException {
        super(gestore,"");
    }
    public CommandTelecomando(Client gestore) throws CommandException {
        super(gestore,"");
    }

    @Override
    public void execute() throws CommandException, ErrorLogException{
        
        this.getGestore().modTelecomando();
        
    }
}