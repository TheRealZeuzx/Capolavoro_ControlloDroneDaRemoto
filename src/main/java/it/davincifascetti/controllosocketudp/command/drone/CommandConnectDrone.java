package it.davincifascetti.controllosocketudp.command.drone;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandInviaMsgClient;
import it.davincifascetti.controllosocketudp.command.CommandSelectClient;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.TerminaleDrone;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandConnectDrone implements Command{

    private GestoreClientServer gestore = null;
    private Ui ui = null;
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il primo costruttore di server
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il server
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @throws CommandableException
     */
    public CommandConnectDrone(GestoreClientServer gestore, String empty,Ui ui) throws CommandException {
        if(ui == null) throw new CommandException("Errore la UI è null!");
        this.ui = ui;
        this.gestore = gestore;
    }

    /**utilizza il metodo newServer 
     * 
     */
    @Override
    public void execute() throws CommandException, ErrorLogException {
        new CommandSelectClient(gestore, "drone", ui).execute();
        new CommandInviaMsgClient((Client)((TerminaleDrone)this.ui).getCli().getGestoreAttuale(), "command", ui).execute();
    }


    
}
