package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;

public class CommandInviaMsgDefaultToClient extends CommandI<ServerThread>{

    public CommandInviaMsgDefaultToClient(ServerThread gestore,String params) throws CommandException{
        super(gestore,params);
    }


    @Override
    public void execute() throws CommandException, ErrorLogException {

        try {
            this.getGestore().inviaMsg("il comando '"+ this.getParams() + "' non è riconosciuto, (digita '$help' per mostrare lista di comandi disponibili)");
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        
    }
}