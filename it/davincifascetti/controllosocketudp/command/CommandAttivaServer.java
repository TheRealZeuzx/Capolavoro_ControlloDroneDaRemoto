package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Server;

public class CommandAttivaServer extends CommandI<Server>{
    public CommandAttivaServer(Server gestore) throws CommandException {
        super(gestore);
    }
    public void execute() throws CommandException {
            try {
                this.getGestore().iniziaAscolto();
            } catch (CommandableException e) {
                throw new CommandException(e.getMessage());
            }
    }
    

}
