package it.davincifascetti.controllosocketudp.program;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.UndoableCommand;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

public abstract class Component {
    private Ui riferimentoUi = null;
    private CommandListManager manager = null;

    

    public CommandListManager getManager(){
        return this.manager;
    }
    public void setManager (CommandListManager manager) throws CommandException{
        if(manager == null) throw new CommandException("Errore, il gestore inserito è null!");
        this.manager = manager;
    }
    public  void setUi(Ui ui) throws CommandException{
        if(ui == null) throw new CommandException("Errore, la UI passata è null!");
        this.riferimentoUi = ui;
    }
    public Ui getUi(){return this.riferimentoUi;}
    private Map<Commandable,CommandHistory> storieComandi = Collections.synchronizedMap(new HashMap<Commandable,CommandHistory>());


    /**
     * 
     * @return CommandHistory appartenente al gestore attuale
     */
    public CommandHistory getCommandHistory(Commandable gestore){
        CommandHistory temp = this.findCommandHistory(gestore);
        if(temp == null){
            temp =  new CommandHistory();
            this.storieComandi.put(gestore, temp);
        }
        return temp;
    }
    

    public void add(Commandable commandable){
        CommandHistory temp = this.findCommandHistory(commandable);
        if(temp == null) this.storieComandi.put(commandable, new CommandHistory());
    }

    private CommandHistory findCommandHistory(Commandable commandable){
        boolean pos = this.storieComandi.containsKey(commandable);
        if(!pos)
            return null;
        return this.storieComandi.get(commandable);
    }

    public void remove(Commandable commandable){
        if(commandable == null) return;
        this.storieComandi.remove(commandable);
    }

    protected void executeCommand(Command command,Commandable gestore) throws CommandException,ErrorLogException{
        if(command == null) return;
        command.execute();
        if(command instanceof UndoableCommand)this.getCommandHistory(gestore).push((UndoableCommand)command);
        
    }

    protected boolean undo(Commandable gestore) throws CommandException, ErrorLogException {
        if (this.getCommandHistory(gestore).isEmpty()) return false;
        UndoableCommand command = this.getCommandHistory(gestore).pop();
        if (command != null) {
            return command.undo();
        }
        return false;
    }
}
