package it.davincifascetti.controllosocketudp.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**permette di gestire una map di CommandList identificate dalla classe es devo avere una lista per Client e una per Server distinte in modo da poter avere chiavi omonime per le due liste
 * @author Tommaso Mussaldi
 */
public class CommandListManager {

    private Map<Class<? extends Commandable>,CommandList> listeComandi = null;
    public CommandListManager(){
        this.listeComandi = Collections.synchronizedMap(new HashMap<Class<? extends Commandable>,CommandList>());;
    }
    /**permette di recuperare una lista di comandi a partire da una classe che estende Commandable
     * 
     * @param gestoreClass classe del gestore del quale si vuole prendere la lista di comandi
     * @return lista di comandi oppure null
     */
    public CommandList getCommandList(Class<? extends Commandable> gestoreClass){
        if(this.listeComandi.containsKey(gestoreClass))
            return this.listeComandi.get(gestoreClass);
        else{
            System.out.println("Debug: > Creazione Lista Comandi " + gestoreClass);
            CommandList n = new CommandList();
            this.listeComandi.put(gestoreClass,n);
            return n;
        }
    }

}
