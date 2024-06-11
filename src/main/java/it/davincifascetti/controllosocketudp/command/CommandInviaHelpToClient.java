package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Cli;
import it.davincifascetti.controllosocketudp.program.ServerThread;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandInviaHelpToClient extends CommandI<ServerThread>{

    public CommandInviaHelpToClient(ServerThread gestore,String params,Ui ui) throws CommandException{
        super(gestore,params,ui);
    }


    @Override
    public void execute() throws CommandException, ErrorLogException {

        try {
            this.getGestore().inviaMsg(this.getUi().getUser().getManager(Cli.class).getCommandList(this.getGestore().getClass()).getStringaHelp());
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        
    }

}