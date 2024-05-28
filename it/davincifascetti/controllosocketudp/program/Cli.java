package it.davincifascetti.controllosocketudp.program;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.program.user.User;


public class Cli implements Component,EventListenerRicezioneBuffer,EventListenerCommandable{

    private CommandListManager manager;
    private CommandFactoryI factory;
    private Commandable gestoreAttuale;
    private Map<Commandable,CommandHistory> storieComandi = Collections.synchronizedMap(new HashMap<Commandable,CommandHistory>());
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
    public void setUi(Ui ui) throws CommandException {
        if(ui == null) throw new CommandException("Errore, la UI passata è null!");
        this.riferimentoUi = ui;
    }

    //TODO crea una classe apposita per gestire le viste
    /**cambia la vista attuale
     * 
     */
    public void setVista(){

    }

    /**
     * 
     * @return CommandHistory appartenente al gestore attuale
     */
    public CommandHistory getCommandHistory(){

    }
    



    @Override
    public void update(byte[] buffer, int lung,Commandable commandable) {
        System.out.println("messaggio ricevuto!");
    }
    @Override
    public void update(String eventType, Commandable commandable) {
        System.out.println("è appena successa una cosa epica: " + eventType);
    }

}
