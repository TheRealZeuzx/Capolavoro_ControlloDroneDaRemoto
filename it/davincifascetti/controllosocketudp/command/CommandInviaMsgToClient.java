package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;

public class CommandInviaMsgToClient extends CommandI<ServerThread>{
    public CommandInviaMsgToClient(ServerThread gestore,String params) throws CommandException{
        super(gestore,params);
    }


    @Override
    public void execute() throws CommandException, ErrorLogException {

        try {
            this.getGestore().inviaMsg(this.getParams());
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        
    }

}
