package it.davincifascetti.controllosocketudp.command.drone;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandSelectClient;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandPilot implements Command{
    private CommandSelectClient command = null;
    public CommandPilot(GestoreClientServer gest,String nome,Ui ui) throws CommandException{
        this.command = new CommandSelectClient(gest, "drone", ui);
    }
    @Override
    public void execute() throws CommandException, ErrorLogException {
        this.command.execute();
    }
}
