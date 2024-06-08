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
    private String porta = null;
    private String ip = null;
    private String ipPrecedente = null;
    private String portaPrecedente = null;
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
        else if(temp.length == 2){
            this.ip = temp[0];
            this.ipPrecedente = this.getGestore().getIp();
            this.porta = temp[1];
            this.portaPrecedente = String.valueOf(this.getGestore().getPorta());
        }else
            throw new CommandException("I parametri passati non sono validi!");
        
    }
    @Override
    /**elimina il socket e imposta lo stato a disattivo quindi una volta modificato il socket devi riattivare il server
     * 
     */
    public void execute() throws CommandException,ErrorLogException {
        try {
            if(this.porta == null || this.ip== null)
            throw new CommandException("Errore, qualcosa è andato storto!");
            else if(porta != null && ip != null)  this.getGestore().setSocket(ip,porta);
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

