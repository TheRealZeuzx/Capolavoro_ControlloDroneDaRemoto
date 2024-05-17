package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Server;

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
        @param nome Nome logico del server da disattivare.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandDisattivaServer(Server gestore) throws CommandException {
        super(gestore);
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
