package it.davincifascetti.controllosocketudp.program;

import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;


public class Cli implements Component,EventListenerRicezioneString{

    private CommandListManager manager;


    private CommandFactoryI factory;



    public Cli(CommandListManager manager){
        this.manager = manager;
    }


    @Override
    public CommandListManager getManager() {
        return this.manager;
    }


    @Override
    public void update(String msg) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
