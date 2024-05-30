package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.Ui;
/**cambia il nome del client
 * dispone di metodo undo quindi è possibile revertire i cambiamenti
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandSetNomeClient extends CommandI<Client> implements UndoableCommand{
    private String nomePrecedente;
    /**
     * @param gestore Client a cui cambiare il nome
     * @param nome nuovo nome 
     * @throws CommandException
     */
    public CommandSetNomeClient(Client gestore,String nome,Ui ui) throws CommandException {
        super(gestore,nome,ui);
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
