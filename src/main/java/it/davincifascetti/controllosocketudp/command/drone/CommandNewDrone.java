package it.davincifascetti.controllosocketudp.command.drone;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandNewClient;
import it.davincifascetti.controllosocketudp.command.CommandNewServer;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.command.UndoableCommand;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandNewDrone implements UndoableCommand{

    private CommandNewServer newServer = null;
    private CommandNewClient newClient = null;
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il primo costruttore di server
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il server
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @throws CommandableException
     */
    public CommandNewDrone(GestoreClientServer gestore, String empty,Ui ui) throws CommandException {
        this.newClient = new CommandNewClient(gestore, "drone 192.168.10.1 8889", ui);
        this.newServer = new CommandNewServer(gestore, "droneVideoWIP", ui, "video");
    }

    /**utilizza il metodo newServer 
     * 
     */
    @Override
    public void execute() throws CommandException, ErrorLogException {
        this.newClient.execute();
        this.newServer.execute();
    }

    /**permette di eliminare il server appena creatp
     * 
     */
    @Override
    public boolean undo() throws CommandException {
        this.newClient.undo();
        this.newServer.undo();
        return true;
    }
    
}
