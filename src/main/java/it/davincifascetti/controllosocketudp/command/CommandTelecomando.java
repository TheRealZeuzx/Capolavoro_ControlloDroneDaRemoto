package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.Terminal;
import it.davincifascetti.controllosocketudp.program.Ui;

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
    public CommandTelecomando(Client gestore,String params,Ui ui) throws CommandException {
        super(gestore,"",ui);
        if(!Terminal.class.isInstance(this.getUi())) throw new CommandException("La Ui fornita non è un Terminal!");
    }
    public CommandTelecomando(Client gestore,Ui ui) throws CommandException {
        super(gestore,"",ui);
    }

    @Override
    public void execute() throws CommandException, ErrorLogException{
        ((Terminal)this.getUi()).getGestoreRemote().modTelecomando(this.getGestore());
    }
}