package it.davincifascetti.controllosocketudp.program.user;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.program.Component;;

/**classe User è una sorta di wrapper per Error e GestoreClientServer, in modo da evitare all'utente di occuparsi della creazione del errorLog e GestoreClientServer
 * che saranno i componenti principali
 * Per estendere User devo rendere la classe final
 * Se voglio estendere un figlio di User devo creare un figlio di User e poi wrappare la classe figlio che voglio "estendere"
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class User {

    //TODO cambiare eccezione
    private final Map<Class<? extends Component>, CommandListManager> listeManagers = Collections.synchronizedMap(new HashMap<Class<? extends Component>, CommandListManager>());
    private String nome;
    /**
     * @param pathErrorLogFile path del file errori
     * @param clazz Class che estende User (deve essere la classe di tipo figlio)
     * @throws CommandException
     */
    //se voglio registrarli partendo da un file xml
    public User(File file) throws CommandException{
        //parse file
        System.out.println("USER " + this.nome);

    }
    //se voglio registrarli manualmente da codice
    public User(String nome) throws CommandException{
        this.nome = nome;
        System.out.println("USER " + this.nome);
    }


    /**se la chiave esiste già non viene creato
     * 
     * @param clazz non può essere null
     * @return
     */
    public CommandListManager getManager(Class<? extends Component> clazz){
        if(clazz == null) return null;
        if(this.listeManagers.containsKey(clazz))
            return this.listeManagers.get(clazz);
        else{
            System.out.println("Debug: > Creazione Lista Comandi " + clazz == null? "" : clazz.getSimpleName());
            CommandListManager n = new CommandListManager();
            this.listeManagers.put(clazz,n); 
            return n;
        }
        
    }

}
