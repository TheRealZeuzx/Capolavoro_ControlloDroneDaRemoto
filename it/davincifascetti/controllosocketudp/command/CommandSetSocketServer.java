package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Server;
/**elimina il socket e imposta lo stato a disattivo quindi una volta modificato il socket devi riattivare il server
 * dispone di metodo undo quindi è possibile revertire i cambiamenti
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandSetSocketServer extends CommandI<Server> implements UndoableCommand{
    private String porta;
    private String portaPrecedente;
    /**
     * 
     * @param gestore Server a cui verrà cambiato la socket
     * @param porta nuova porta locale
     * @throws CommandException
     */
    public CommandSetSocketServer(Server gestore,String porta) throws CommandException {
        super(gestore);
        this.porta = porta;
        this.portaPrecedente = String.valueOf(this.getGestore().getPorta());
    }
    @Override
    /**elimina il socket e imposta lo stato a disattivo quindi una volta modificato il socket devi riattivare il server
     * 
     */
    public void execute() throws CommandException,ErrorLogException {
        try {
            this.getGestore().setSocket(porta);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        
    }
    @Override
    public boolean undo() throws CommandException,ErrorLogException{
        try {
            this.getGestore().setSocket(portaPrecedente);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        return true;
    }
}
