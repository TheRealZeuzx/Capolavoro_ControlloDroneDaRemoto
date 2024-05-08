package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;

/**
    CommandDeleteClient. 
    È ciò occupa di eliminare un client istanziato dal terminale.
    Identifica il client da eliminare dal nome logico assegnatogli al momento della creazione.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandDeleteClient extends CommandI<GestoreClientServer> implements UndoableCommand{
    private String nome;
    private Client client;
    /**
        Costruttore di default di CommandDeleteClient.
        Richiama al costruttore del padre (CommandI).
        @param gestore Gestore su cui effettuare le operazioni. 
        @param nome Nome logico del client da eliminare.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandDeleteClient(GestoreClientServer gestore,String params) throws CommandException {
        super(gestore, params);
        this.nome = params;
    }

    /**
        Si occupa di identificare il client (in base al nome logico) e di chiamare il metodo corrispondente
        del terminale per eliminare quel client. 
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
     */
    public void execute() throws CommandException {
        try {
            this.client = this.getGestore().ricercaClient(nome);
            this.getGestore().removeClient(this.client);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }
    /**
        Si occupa di annullare l'eliminazione del client. 
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
        @throws ErrorLogException Eccezione sollevata per scrivere sul file di logging degli errori.
     */
    @Override
    public boolean undo() throws CommandException,ErrorLogException{
        try {
            this.getGestore().addClient(this.client);
        } catch (CommandableException e) {
            throw new ErrorLogException("Errore, impossibile ripristinare il server");
        }
        return true;
    }
}

