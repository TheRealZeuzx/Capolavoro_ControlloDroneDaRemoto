package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandInviaMsgToClient extends CommandI<ServerThread>{
    public CommandInviaMsgToClient(ServerThread gestore,String params,Ui ui) throws CommandException{
        super(gestore,params,ui);
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
