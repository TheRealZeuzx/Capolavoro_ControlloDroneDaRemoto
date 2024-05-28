package it.davincifascetti.controllosocketudp.program;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;


public class Cli implements Component,EventListenerRicezioneString{

    private CommandListManager manager;
    private CommandFactoryI factory;
    private Ui riferimentoUi;


    public Cli(CommandListManager manager) throws CommandException{
        this.factory = new CommandFactoryI();
        this.setManager(manager);
    }
    public Cli(CommandListManager manager,Ui ui) throws CommandException{
        this.factory = new CommandFactoryI();
        this.setManager(manager);
        this.setUi(ui);
    }


    @Override
    public CommandListManager getManager() {
        return this.manager;
    }

    public void setManager (CommandListManager manager) throws CommandException{
        if(manager == null) throw new CommandException("Errore, il gestore inserito è null!");
        this.manager = manager;
        this.factory.setManager(manager);
    }

    @Override
    public void update(String msg) {
        // risposta del server 
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }


    @Override
    public void setUi(Ui ui) throws CommandException {
        if(ui == null) throw new CommandException("Errore, la UI passata è null!");
        this.riferimentoUi = ui;
    }

}
