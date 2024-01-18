package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
/**cambia i valori di ip e porta del server remoto
 * dispone di metodo undo quindi è possibile revertire i cambiamenti
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandSetSocketClient extends CommandI<Client> implements UndoableCommand{
    private String porta;
    private String ip;
    private String ipPrecedente;
    private String portaPrecedente;
    /**
     * 
     * @param gestore Client a cui verrà cambiato la socket
     * @param ip nuovo ip di destinazione
     * @param porta nuova porta di destinazione
     * @throws CommandException
     */
    public CommandSetSocketClient(Client gestore,String ip,String porta) throws CommandException {
        super(gestore);
        this.ip = ip;
        this.ipPrecedente = this.getGestore().getIp();
        this.porta = porta;
        this.portaPrecedente = String.valueOf(this.getGestore().getPorta());
    }
    @Override
    public void execute() throws CommandException,ErrorLogException {
        try {
            this.getGestore().setSocket(ip,porta);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        
    }
    @Override
    public boolean undo() throws CommandException,ErrorLogException{
        try {
            this.getGestore().setSocket(ipPrecedente,portaPrecedente);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        return true;
    }
}
