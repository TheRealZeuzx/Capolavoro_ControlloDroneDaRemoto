package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Server;
/**cambia il nome del server
 * dispone di metodo undo quindi è possibile revertire i cambiamenti
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandSetNomeServer extends CommandI<Server> implements UndoableCommand{
    private String nomePrecedente;
    /**
     * 
     * @throws CommandException
     */
    public CommandSetNomeServer(Server gestore,String nome) throws CommandException {
        super(gestore,nome);
        this.nomePrecedente = this.getGestore().getNome();
    }
    @Override
    public void execute() throws CommandException {
        try {
            this.getGestore().setNome(this.getParams());
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
