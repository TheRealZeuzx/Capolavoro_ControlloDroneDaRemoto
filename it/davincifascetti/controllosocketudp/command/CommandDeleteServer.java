package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Ui;

/**
 * SERVER
    CommandDeleteServer. 
    È ciò occupa di eliminare un server istanziato dal terminale.
    Identifica il server da eliminare dal nome logico assegnatogli al momento della creazione.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandDeleteServer extends CommandI<GestoreClientServer> implements UndoableCommand{
    private String nome;
    private Server server;
    /**
        Costruttore di default di CommandDeleteServer.
        Richiama al costruttore del padre (CommandI).
        @param gestore Gestore su cui effettuare le operazioni. 
        @param nome Nome logico del server da eliminare.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandDeleteServer(GestoreClientServer gestore,String nome,Ui ui) throws CommandException {
        super(gestore,nome,ui);
        this.nome = nome;
    }
    @Override
    /**
        Si occupa di identificare il server (in base al nome logico) e di chiamare il metodo corrispondente
        del terminale per eliminare quel server. 
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
     */
    public void execute() throws CommandException {
        try {
            this.server = this.getGestore().ricercaServer(nome);
            if(server == null) throw new CommandException("Il server '" + nome +"' non è stato trovato!");
            this.server.terminaAscolto();
            this.getGestore().removeServer(this.server);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
        Si occupa di annullare l'eliminazione del server. 
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
        @throws ErrorLogException Eccezione sollevata per scrivere sul file di logging degli errori.
     */
    @Override
    public boolean undo() throws CommandException,ErrorLogException{
        try {
            if(this.server != null){ 
                this.server.iniziaAscolto();
                this.getGestore().addServer(this.server);
            }
        } catch (CommandableException e) {
            throw new ErrorLogException("Errore, impossibile ripristinare il server");
        }
        return true;
    }
}
