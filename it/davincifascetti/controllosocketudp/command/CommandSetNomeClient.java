package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;

public class CommandSetNomeClient extends CommandI<Client> implements UndoableCommand{
    private String nome;
    private String nomePrecedente;
    public CommandSetNomeClient(Client gestore,String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
        this.nomePrecedente = this.getGestore().getNome();
    }
    @Override
    /**elimina il Client e imposta lo stato a disattivo quindi se fai undo devi riattivarlo
     * 
     */
    public void execute() throws CommandException {
        try {
            this.getGestore().setNome(nome);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        
    }
    @Override
    public boolean undo() throws CommandException,ErrorLogException{
        try {
            this.getGestore().setNome(nomePrecedente);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        return true;
    }
}
