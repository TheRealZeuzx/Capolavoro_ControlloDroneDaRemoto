package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.ServerThread;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandInviaMsgDefaultToClient extends CommandI<ServerThread>{

    public CommandInviaMsgDefaultToClient(ServerThread gestore,String params,Ui ui) throws CommandException{
        super(gestore,params,ui);
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