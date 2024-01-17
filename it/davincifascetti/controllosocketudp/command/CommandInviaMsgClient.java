package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;

public class CommandInviaMsgClient extends CommandI<Client>{
    private String msg;
    public CommandInviaMsgClient(String msg, Client gestore) throws CommandException {
        super(gestore);
        this.msg = msg;
    }

    @Override
    public void execute() throws CommandException, ErrorLogException {
        try {
            this.getGestore().inviaMsg(this.msg);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }

}
