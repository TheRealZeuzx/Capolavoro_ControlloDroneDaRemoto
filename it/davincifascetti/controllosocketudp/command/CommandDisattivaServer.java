package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Server;

public class CommandDisattivaServer extends CommandI<Server>{
    public CommandDisattivaServer(Server gestore) throws CommandException {
        super(gestore);
    }
    public void execute() throws CommandException {
            this.getGestore().terminaAscolto();
    }
    

}
