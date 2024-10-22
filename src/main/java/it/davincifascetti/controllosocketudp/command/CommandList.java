package it.davincifascetti.controllosocketudp.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
/**
 * @author Tommaso Mussaldi
 */
public class CommandList {

    private Map<String,String> arrayAssociativo = Collections.synchronizedMap(new HashMap<String,String>());
    private String comandoDefault = null;
    private String help = null;
   /** permette la registrazione di un comando, viene fatto dal gestore stesso che si occuperà di registrare tutti i comandi di cui necessita
     * @param call stringa con la quale viene identificato il comando (usare una regexp che identifichi la parte necessaria a richiamare il comando)
     * @param CommandClass la classe del comando deve extendere CommandI<T extends Commandable> (deve avere il costruttore con parametri Commandable,String)
     * @param defaultCommand valore booleano che indica se il comando verrà impostato come comando di default (c'è un solo comando di default), nel caso in cui sia impostato a true, la stringa call verrà ignorata
     * inizialmente il comando di default è impostato su CommandDefault
     * @throws CommandException
     */
    private void registrazioneComando(String call,String CommandClass,boolean defaultCommand) throws CommandException{
        System.out.println("Debug: " + CommandClass + " | registrazione...");
        if(((defaultCommand) || (call != null && !call.isBlank())) && CommandClass != null  && !CommandClass.isBlank()){
            try { 
                if(Command.class.isAssignableFrom(Class.forName(CommandClass)))
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
    /**
     *  
     * permette la registrazione di un comando, viene fatto dal gestore stesso che si occuperà di registrare tutti i comandi di cui necessita
     * Le eccezioni sono gestite internamente in modo da non impedire la continuazione del programma (il che rende possibile registrare comandi a runtime)
     * @param call stringa con la quale viene identificato il comando (usare una regexp)
     * @param CommandClass la classe del comando deve extendere CommandI<T extends Commandable> (deve avere il costruttore con parametri Commandable,String)
     */
    //wrappo con registraComando in modo che le eccezioni siano gestite direttamente in questo metodo e i comandi successivi vengano eseguiti nonostante l'errore (programma non termina in caso eccezione)
    public void registraComando(String call,String CommandClass){
        try {
            this.registrazioneComando(call,CommandClass,false);
        } catch (CommandException e) {
            System.err.println("\033[0;31m"+e.getMessage()+"\033[0;37m");
        }
    }
    /**permette la registrazione di un comando, viene fatto dal gestore stesso che si occuperà di registrare tutti i comandi di cui necessita
     * Le eccezioni sono gestite internamente in modo da non impedire la continuazione del programma (il che rende possibile registrare comandi a runtime)
     * @param call stringa con la quale viene identificato il comando (usare una regexp che identifichi la parte necessaria a richiamare il comando)
     * @param CommandClass la classe del comando deve extendere CommandI<T extends Commandable> (deve avere il costruttore con parametri Commandable,String)
     * @param defaultCommand valore booleano che indica se il comando verrà impostato come comando di default (c'è un solo comando di default), nel caso in cui sia impostato a true, la stringa call verrà ignorata
     * inizialmente il comando di default è impostato sulla classe  it.davincifascetti.controllosocketudp.command.CommandDefault
     */
    //wrappo con registraComando in modo che le eccezioni siano gestite direttamente in questo metodo e i comandi successivi vengano eseguiti nonostante l'errore (programma non termina in caso eccezione)
    public void registraComando(String call,String CommandClass,boolean defaultCommand){
        try {
            this.registrazioneComando(call,CommandClass,defaultCommand);
        } catch (CommandException e) {
            System.err.println("\033[0;31m"+e.getMessage()+"\033[0;37m");
        }
    }


    /**
     * 
     * @return resituisce la copia della Map contenente i Comandi
     */
    public Map<String,String> getComandi(){
        if(this.arrayAssociativo.isEmpty())
            return null;
        return Map.copyOf(this.arrayAssociativo);
    }
    public String getCommandDefault(){return this.comandoDefault;}
    public String getStringaHelp(){if(this.help == null || this.help.isBlank()) return "Non è stata definita una stringa di help"; else return this.help;}
    public void setStringaHelp(String help){this.help = help;}

}
