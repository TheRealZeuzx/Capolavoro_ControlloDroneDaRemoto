package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;

public class CommandSetSocketClient extends CommandI<Client> implements UndoableCommand{
    private String porta;
    private String ip;
    private String ipPrecedente;
    private String portaPrecedente;
    public CommandSetSocketClient(Client gestore,String ip,String porta) throws CommandException {
        super(gestore);
        this.ip = ip;
        this.ipPrecedente = this.getGestore().getIp();
        this.porta = porta;
        this.portaPrecedente = String.valueOf(this.getGestore().getPorta());
    }
    @Override
    /**elimina il Client e imposta lo stato a disattivo quindi se fai undo devi riattivarlo
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
            this.getGestore().setSocket(ipPrecedente,portaPrecedente);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        return true;
    }
}
