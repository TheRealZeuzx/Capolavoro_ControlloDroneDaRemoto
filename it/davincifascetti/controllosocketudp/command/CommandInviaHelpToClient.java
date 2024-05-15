package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;

public class CommandInviaHelpToClient extends CommandI<ServerThread>{

    public CommandInviaHelpToClient(ServerThread gestore,String params) throws CommandException{
        super(gestore,params);
    }


    @Override
    public void execute() throws CommandException, ErrorLogException {

        try {
            this.getGestore().inviaMsg(Commandable.ListeComandi.getCommandList(this.getGestore().getClass()).getStringaHelp());
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        
    }

}