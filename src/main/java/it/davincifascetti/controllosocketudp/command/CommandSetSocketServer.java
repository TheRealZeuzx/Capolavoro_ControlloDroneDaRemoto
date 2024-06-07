package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Ui;
/**elimina il socket e imposta lo stato a disattivo quindi una volta modificato il socket devi riattivare il server
 * dispone di metodo undo quindi è possibile revertire i cambiamenti
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandSetSocketServer extends CommandI<Server> implements UndoableCommand{
    private String porta;
    private String ip;
    private String ipPrecedente;
    private String portaPrecedente;
    /**
     * 
     * @param gestore Server a cui verrà cambiato la socket
     * @param porta nuova porta locale
     * @throws CommandException
     */
    public CommandSetSocketServer(Server gestore,String ipPorta,Ui ui) throws CommandException {
        super(gestore,ipPorta,ui);
        String []temp =  this.getParams().split("[ ]+");
        if(temp.length != 2) throw new CommandException("Errore, il numero di parametri è errato!");
        this.porta = temp[0];
        this.portaPrecedente = String.valueOf(this.getGestore().getPorta());;
        this.ip = temp[1];
        this.ipPrecedente = this.getGestore().getIp();
    }
    @Override
    /**elimina il socket e imposta lo stato a disattivo quindi una volta modificato il socket devi riattivare il server
     * 
     */
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

