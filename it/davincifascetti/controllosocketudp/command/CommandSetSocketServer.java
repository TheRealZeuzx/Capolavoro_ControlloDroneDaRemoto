package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Server;

public class CommandSetSocketServer extends CommandI<Server> implements UndoableCommand{
    private String porta;
    private String portaPrecedente;
    public CommandSetSocketServer(Server gestore,String porta) throws CommandException {
        super(gestore);
        this.porta = porta;
        this.portaPrecedente = String.valueOf(this.getGestore().getPorta());
    }
    @Override
    /**elimina il server e imposta lo stato a disattivo quindi se fai undo devi riattivarlo
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
