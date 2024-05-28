package it.davincifascetti.controllosocketudp.program.user;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandListManager;

/**classe User è una sorta di wrapper per Error e GestoreClientServer, in modo da evitare all'utente di occuparsi della creazione del errorLog e GestoreClientServer
 * che saranno i componenti principali
 * Per estendere User devo rendere la classe final
 * Se voglio estendere un figlio di User devo creare un figlio di User e poi wrappare la classe figlio che voglio "estendere"
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public abstract class User {

    //!problema: così i listmanager sono specifici per la CLi ma non dovrebbero esserlo, dobrebbe essercene una per ogni componente della UI 
    //TODO capire come affrontare il problema e risolvero, per ora userò la classe User per Cli
    private static final Map<Class<? extends User>, CommandListManager> listeManagers = Collections.synchronizedMap(new HashMap<Class<? extends User>, CommandListManager>());
    private static final Map<Class<? extends User>, AtomicBoolean> listeInizialized = Collections.synchronizedMap(new HashMap<Class<? extends User>, AtomicBoolean>());
    /**
     * @param pathErrorLogFile path del file errori
     * @param clazz Class che estende User (deve essere la classe di tipo figlio)
     * @throws CommandException
     */
    protected User(Class<? extends User> clazz) throws CommandException{
        if (!getClass().getSuperclass().equals(User.class)) {
            throw new RuntimeException("Non puoi estendere la classe più di una volta!");
        }
        this.init(clazz);
    }

    protected abstract void registraComandiClient();
    protected abstract void registraComandiServer();
    protected abstract void registraComandiGestoreCS();
    protected abstract void registraComandiServerThread();

    private void init(Class<? extends User> clazz){
        if(User.getListaInit(clazz).compareAndSet(false, true)){
            System.out.println("Debug: | Registrazione comandi GestoreClientServer |");
            this.registraComandiGestoreCS();
            System.out.println("Debug: | Registrazione comandi Client |");
            this.registraComandiClient();
            System.out.println("Debug: | Registrazione comandi Server |");
            this.registraComandiServer();
            System.out.println("Debug: | Registrazione comandi ServerThread |");
            this.registraComandiServerThread();
        }
    }

    /**
     * 
     * @param clazz classe dello user del quale si vuole il CommandListManager
     * !se inserisco una classe errata, i comandi di tutti gli user potrebbero essere sballati
     * @return CommandListManager corrispondente alla classe
     */
    public CommandListManager getManager(){
        if(User.listeManagers.containsKey(this.getClass()))
            return User.listeManagers.get(this.getClass());
        else{
            System.out.println("Debug: > Creazione Lista Comandi " + this.getClass());
            CommandListManager n = new CommandListManager();
            User.listeManagers.put(this.getClass(),n); 
            return n;
        }
    }
    private static AtomicBoolean getListaInit(Class<? extends User> clazz){
        if(User.listeInizialized.containsKey(clazz))
            return User.listeInizialized.get(clazz);
        else{
            System.out.println("Debug: > Creazione Lista Comandi " + clazz);
            AtomicBoolean n = new AtomicBoolean();
            User.listeInizialized.put(clazz,n);
            return n;
        }
    }
}
