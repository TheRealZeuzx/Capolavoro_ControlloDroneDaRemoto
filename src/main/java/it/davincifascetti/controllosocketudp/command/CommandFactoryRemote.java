package it.davincifascetti.controllosocketudp.command;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Vector;

import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.Ui;

public class CommandFactoryRemote implements CommandFactory{

    private Map<String,String> arrayAssociativo = null;
    private String comandoDefault = null;
    private CommandListManager manager = null;
    public CommandFactoryRemote (CommandListManager manager){
        super(); 
        this.manager = manager;
        this.comandoDefault = this.manager.getCommandList(null).getCommandDefault();
    }

    public Command getCommand(Client client,String params, Ui ui) throws CommandException { 
        
        if(client == null) new CommandDefault("Client è null!");
        if(params == null) new CommandDefault("Parametri sono null!");
        if(ui == null) new CommandDefault("Ui è null!");
        
        Class<? extends Commandable> gestoreClass = null;
        //i comandi sono registrati dalla classe gestore
        this.comandoDefault = manager.getCommandList(gestoreClass).getCommandDefault();
        //usando Map.copyOf viene restituita una Map non modificabile
        this.arrayAssociativo = manager.getCommandList(gestoreClass).getComandi();
        if(this.arrayAssociativo == null) throw new CommandException("Errore, il gestore non ha comandi registrati");


        Vector<Object> arguments = new Vector<>();
        Command temp = null;
        arguments.add(client);
        //TODO cambiare con un while
        for(Map.Entry<String, String> entry : this.arrayAssociativo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue(); // comando di riferimento
            if(key.equalsIgnoreCase(params)){
                try {
                    arguments.add(params);
                    arguments.add(ui);
                    if(Class.forName(value).getDeclaredConstructors()[0].getParameterTypes()[0].isInterface())//nel caso in cui sia generale
                        temp = (Command)Class.forName(value).getDeclaredConstructor(Client.class,String.class,Ui.class).newInstance(arguments.toArray());
                    else
                        temp = (Command)Class.forName(value).getDeclaredConstructor(Client.class,String.class,Ui.class).newInstance(arguments.toArray());
                } catch (InvocationTargetException e){
                    throw new CommandException(e.getTargetException().getMessage());
                }catch (Exception e){
                    throw new CommandException(e.getMessage());
                }
            }
        }
    
    
        //se ho impostato il comando di default lo istanzio altrimenti uso CommandDefault
        try {
            if(temp == null && this.comandoDefault != null){
                arguments.add(params);
                arguments.add(ui);
                temp = (Command)Class.forName(this.comandoDefault).getDeclaredConstructor(Client.class,String.class,Ui.class).newInstance(arguments.toArray());
            }
        } catch (Exception e){
            throw new CommandException(e.getMessage());
        }
        return temp == null ? new CommandDefault(params) : temp;
    }
    public void setManager(CommandListManager manager){this.manager = manager;}

    @Override
    public Command getCommand(String params, Ui ui) throws CommandException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCommand'");
    }
}
