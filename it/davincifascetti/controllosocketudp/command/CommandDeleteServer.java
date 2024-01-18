package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;

/**
    
 */
public class CommandDeleteServer extends CommandI<GestoreClientServer> implements UndoableCommand{
    private String nome;
    private Server server;
    public CommandDeleteServer(GestoreClientServer gestore,String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
    }
    @Override
    /**elimina il server e imposta lo stato a disattivo quindi se fai undo devi riattivarlo
     * 
     */
    public void execute() throws CommandException {
        try {
            this.server = this.getGestore().ricercaServer(nome);
            this.server.terminaAscolto();
            this.getGestore().removeServer(this.server);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }
    @Override
    public boolean undo() throws CommandException,ErrorLogException{
        try {
            this.getGestore().addServer(this.server);
        } catch (CommandableException e) {
            throw new ErrorLogException("Errore, impossibile ripristinare il server");
        }
        return true;
    }
}
