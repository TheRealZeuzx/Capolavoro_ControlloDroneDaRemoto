package it.davincifascetti.controllosocketudp.command;

import java.util.Map;

import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandFactoryRemote implements CommandFactory{

    private Map<String,String> arrayAssociativo = null;
    private String comandoDefault = null;
    private CommandListManager manager = null;
    @Override
    public Command getCommand(String params, Ui ui) throws CommandException {
        
    }

}
