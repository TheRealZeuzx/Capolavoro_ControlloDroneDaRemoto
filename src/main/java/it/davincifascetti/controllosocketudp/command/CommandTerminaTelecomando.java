package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Ui;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.Terminal;

public class CommandTerminaTelecomando implements Command{

    private String params = null;
    private Ui ui = null;
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il primo costruttore di server
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il server
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @throws CommandableException
     */
    public CommandTerminaTelecomando(Client client,String empty,Ui ui) throws CommandException {
        this.params = empty;
        this.ui = ui;
    }

    /**utilizza il metodo newServer 
     * 
     */
    @Override
    public void execute() throws CommandException{
        ((Terminal)this.ui).getRemote().stop();
    }

}
