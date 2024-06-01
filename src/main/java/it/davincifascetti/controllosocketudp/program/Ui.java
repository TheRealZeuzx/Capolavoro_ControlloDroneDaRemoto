package it.davincifascetti.controllosocketudp.program;

import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.ErrorLogCommand;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.program.user.User;

public abstract class Ui{

    private ErrorLog errorLog = null;
    private User comandi = null;
    private GestoreClientServer business = null;
    private AtomicBoolean inizializzato = new AtomicBoolean();

    //! credo ci debba essere una factory di UI con metodi getTerminal e getGui
    //?lasciarlo qui ? penso di si
    //ogni componente della UI dovrà avere i propri comandi es CLI comandi da tastiera mentre VIdeo usera gli eventi di Server

    public Ui(GestoreClientServer business,ErrorLog errorLog,User comandi) throws CommandException{
        if(business == null) throw new CommandException("Errore, il GestoreClientServer è null!");
        if(errorLog == null) throw new CommandException("Errore,ErrorLog  è null!");
        if(comandi == null) throw new CommandException("Errore,User è null!");
        this.business = business;
        this.errorLog = errorLog;
        this.comandi = comandi;
    }

    //TODO capire se questa classe la estendono solo i componenti della UI es CLI oppure Video oppure se la estende Terminal o GUI.
    //TODO capire come far subscrivere un componente esempio il video agli eventi di un client specifico (credo un manager)
    /**avvia la UI (fa init e fa main)
     * 
     */
    public final void start() throws CommandException{
        inizializzazione();
        main();
    }
    /**logica main
     * 
     * @throws CommandException
     */
    protected abstract void main() throws CommandException;
    /**subscribe di tutti i componenti alla business
     * 
     */
    protected abstract void init();
    /**fa la subscribe agli eventManager di GestoreClientServer (aggiungere che ogni volta che si crea un client server ci si iscrive?)
     * 
     */
    private void inizializzazione() throws CommandException{
        if(inizializzato.compareAndSet(false, true)){
            if(this.business == null || this.errorLog == null) throw new CommandException("Errore, prima imposta un business logic e un errorLog!");
            this.init();
        }
    }

    /**permette di loggare un errore dall esterno del terminale
     * 
     * @param msg messaggio da loggare
     * @param video true se si stampa anche a video altrimenti false  solo su file
     */
    protected synchronized void errorLog(String msg,boolean video){
        if(video)System.out.println(msg);
        new ErrorLogCommand(this.getErrorLog(),msg).execute();
    }

    public GestoreClientServer getBusiness(){
        return this.business;
    }
    public ErrorLog getErrorLog(){
        return this.errorLog;
    }
    public User getUser(){
        return this.comandi;
    }

}
