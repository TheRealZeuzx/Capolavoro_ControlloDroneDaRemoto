package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Ui;

/**
 * SERVER
    CommandDisattivaServer. 
    È ciò occupa di disattivare un server istanziato dal terminale.
    Identifica il server da disattivare dal nome logico assegnatogli al momento della creazione.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandDisattivaServer extends CommandI<Server>{

    /**
        Costruttore di default di CommandDisattivaServer.
        Richiama al costruttore del padre (CommandI).
        @param gestore Gestore su cui effettuare le operazioni. 
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandDisattivaServer(Server gestore,String params,Ui ui) throws CommandException {
        super(gestore,"",ui);
    }

    /**
        Si occupa di identificare il server (in base al nome logico) e di chiamare il metodo corrispondente
        del terminale per disattivare quel server. 
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
     */
    public void execute() throws CommandException {
        this.getGestore().terminaAscolto();
    }
    

}
