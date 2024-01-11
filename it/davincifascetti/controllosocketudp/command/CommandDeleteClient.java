package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;

public class CommandDeleteClient extends CommandI<GestoreClientServer> implements UndoableCommand{
    private String nome;
    private Client client;
    public CommandDeleteClient(GestoreClientServer gestore,String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
    }
    public void execute() throws CommandException {
        try {
            this.client = this.getGestore().ricercaClient(nome);
            this.client.terminaAscolto();
            this.getGestore().removeClient(this.client);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }
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

