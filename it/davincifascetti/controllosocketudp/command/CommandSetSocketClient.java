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
    public CommandSetSocketClient(Client gestore,String ipPorta) throws CommandException {
        super(gestore,ipPorta);
        String []temp =  this.getParams().split("[ ]+");
        if(temp.length != 2) throw new CommandException("Errore, il numero di parametri è errato!");
        this.ip = temp[0];
        this.ipPrecedente = this.getGestore().getIp();
        this.porta = temp[1];
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
            if(portaPrecedente.equals("-1")) this.portaPrecedente = null;
            this.getGestore().setSocket(ipPrecedente,portaPrecedente);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        return true;
    }
}
