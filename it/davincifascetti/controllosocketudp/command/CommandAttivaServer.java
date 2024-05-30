package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Ui;
/**
    SERVER
    Command Attiva Server.
    Si occupa di gestire l'attivazione del server.
    @author Mussaldi Tommaso, Bonfiglio mattia
    @version 1.0
*/
public class CommandAttivaServer extends CommandI<Server>{

    /**
        Costruttore di default di Attiva Server.
        Richiama al costruttore del padre (CommandI).
        @param gestore Gestore su cui effettuare le operazioni. 
    */
    public CommandAttivaServer(Server gestore,String params,Ui ui) throws CommandException {
        super(gestore,"",ui);
    }

    /**
        Attiva la funzione "iniziaAscolto" del gestore corrispondente.
     * @throws ErrorLogException 
     */
    public void execute() throws CommandException, ErrorLogException {
            try {
                this.getGestore().iniziaAscolto();
            } catch (CommandableException e) {
                throw new CommandException(e.getMessage());
            }
    }
    

}
