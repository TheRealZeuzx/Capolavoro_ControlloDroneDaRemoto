package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;

public class CommandSetNomeServer extends CommandI<Server> implements UndoableCommand{
    private String nome;
    private String nomePrecedente;
    public CommandSetNomeServer(Server gestore,String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
        this.nomePrecedente = this.getGestore().getNome();
    }
    @Override
    /**elimina il server e imposta lo stato a disattivo quindi se fai undo devi riattivarlo
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
