package it.davincifascetti.controllosocketudp.command.drone;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandInviaMsgClient;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandRuotaSinistra implements Command{
 private String params = null;
    private Ui ui = null;
    private Client client = null;
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il primo costruttore di server
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il server
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @throws CommandableException
     */
    public CommandRuotaSinistra(Client client, String empty,Ui ui) throws CommandException {
        this.params = empty;
        this.ui = ui;
        this.client = client;
    }

    /**utilizza il metodo newServer 
     * @throws ErrorLogException 
     * 
     */
    @Override
    public void execute() throws CommandException, ErrorLogException{
        new CommandInviaMsgClient(client,"ccw 1", ui).execute();
    }
}
