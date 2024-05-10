package it.davincifascetti.controllosocketudp.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandList {

    private Map<String,String> arrayAssociativo = Collections.synchronizedMap(new HashMap<String,String>());
    private String comandoDefault = null;

   /**permette la registrazione di un comando, viene fatto dal gestore stesso che si occuperà di registrare tutti i comandi di cui necessita
     * @param call stringa con la quale viene identificato il comando (usare una regexp che identifichi la parte necessaria a richiamare il comando)
     * @param CommandClass la classe del comando deve extendere CommandI<T extends Commandable> (deve avere il costruttore con parametri Commandable,String)
     * @param default valore booleano che indica se il comando verrà impostato come comando di default (c'è un solo comando di default), nel caso in cui sia impostato a true, la stringa call verrà ignorata
     * inizialmente il comando di default è impostato su CommandDefault
     * @throws CommandException
     */
    //?nel caso in cui venga inserita una GUI , si può inserire un riferimento ad un bottone o simile come chiave idk
    public void registraComando(String call,String CommandClass,boolean defaultCommand) throws CommandException{
        System.out.println("Debug: " + CommandClass + " | registrazione...");
        if(((call == null && defaultCommand) || call != null) && CommandClass != null && !call.isBlank() && !CommandClass.isBlank()){
            try { 
                if(CommandI.class.isAssignableFrom(Class.forName(CommandClass)))
                    if(!defaultCommand)this.arrayAssociativo.put(call, CommandClass); else this.comandoDefault = CommandClass;
                else
                    throw new CommandException("La classe inserita non implementa 'CommandI'");
            } catch (ClassNotFoundException e) {
                throw new CommandException("errore, la classe inserita non è stata trovata ('" + e.getMessage() + "')");
            }
        }else{
            throw new CommandException("Una delle stringhe inserite non è valida!");
        }
    }
    /**permette la registrazione di un comando, viene fatto dal gestore stesso che si occuperà di registrare tutti i comandi di cui necessita
     * @param call stringa con la quale viene identificato il comando (usare una regexp)
     * @param CommandClass la classe del comando deve extendere CommandI<T extends Commandable> (deve avere il costruttore con parametri Commandable,String)
     * @throws CommandException
     */
    public void registraComando(String call,String CommandClass) throws CommandException{
        this.registraComando(call,CommandClass,false);
    }

    public Map<String,String> getComandi(){
        if(this.arrayAssociativo.isEmpty())
            return null;
        return this.arrayAssociativo;
    }
    public String getCommandDefault(){return this.comandoDefault;}

}
