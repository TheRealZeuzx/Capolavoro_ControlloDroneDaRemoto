package it.davincifascetti.controllosocketudp.command.drone;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandSelectServer;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandStatus implements Command{
    private CommandSelectServer command = null;
    public CommandStatus(GestoreClientServer gest,String nome,Ui ui) throws CommandException{
        this.command = new CommandSelectServer(gest, "state", ui);
    }
    @Override
    public void execute() throws CommandException, ErrorLogException {
        this.command.execute();
    }
}
