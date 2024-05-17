package it.davincifascetti.controllosocketudp.program.user;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;

/**classe User è una sorta di wrapper per Error e GestoreClientServer, in modo da evitare all'utente di occuparsi della creazione del errorLog e GestoreClientServer
 * che saranno i componenti principali
 * Per estendere User devo rendere la classe final
 * Se voglio estendere un figlio di User devo creare un figlio di User e poi wrappare la classe figlio che voglio "estendere"
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public abstract class User {
    private ErrorLog errorLog = null;
    private GestoreClientServer gestore = null;
    private static final Map<Class<? extends User>, CommandListManager> listeManagers = Collections.synchronizedMap(new HashMap<Class<? extends User>, CommandListManager>());
    private static final Map<Class<? extends User>, AtomicBoolean> listeInizialized = Collections.synchronizedMap(new HashMap<Class<? extends User>, AtomicBoolean>());
    private static final AtomicBoolean started = new AtomicBoolean();
    /**
     * @param pathErrorLogFile path del file errori
     * @param clazz Class che estende User (deve essere la classe di tipo figlio)
     * @throws CommandException
     */
    protected User(String pathErrorLogFile,Class<? extends User> clazz) throws CommandException{
        if (!getClass().getSuperclass().equals(User.class)) {
            throw new RuntimeException("Non puoi estendere la classe più di una volta!");
        }
        this.init(clazz);
        try {
            this.errorLog = new ErrorLog(pathErrorLogFile);
            this.gestore = new GestoreClientServer(errorLog,User.getManager(clazz));
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }
    /**avvia il terminale di gestoreclientserver di conseguenza il programma in se
     * 
     * @throws CommandException
     */
    public final void start() throws CommandException{
        if(User.started.compareAndSet(false, true)){
            this.logicaStart();
            User.started.compareAndSet(true, false);
        }else return;
    }
    protected abstract void logicaStart() throws CommandException;
    protected abstract void registraComandiClient();
    protected abstract void registraComandiServer();
    protected abstract void registraComandiGestoreCS();
    protected abstract void registraComandiServerThread();
    public ErrorLog getErrorLog(){return errorLog;}
    
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
    protected static CommandListManager getManager(Class<? extends User> clazz){
        if(User.listeManagers.containsKey(clazz))
            return User.listeManagers.get(clazz);
        else{
            System.out.println("Debug: > Creazione Lista Comandi " + clazz);
            CommandListManager n = new CommandListManager();
            User.listeManagers.put(clazz,n);
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
    public GestoreClientServer getGestore(){return gestore;}
}
