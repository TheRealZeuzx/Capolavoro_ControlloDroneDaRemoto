package it.davincifascetti.controllosocketudp.program;

import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.UndoableCommand;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
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

    /**esegue il comando e restituisce true se l'esec è riuscita altrimenti false, se il comando implementa undoable command, viene inserito nella storia comandi
     * 
     * @param command comando da eseguire (deve implementare Command)
     * @return true se l'esecuzione è andata a buon fine altrimenti false
     * @throws CommandException
     */
    protected abstract void executeCommand(Command command) throws CommandException,ErrorLogException;
    // protected void executeCommand(Command command) throws CommandException,ErrorLogException{
    //     if(command == null) return;
    //     command.execute();
    //     if(command instanceof UndoableCommand)storiaComandi.push((UndoableCommand)command);
        
    // }
    

    /**fa l'undo dell' ultimo undoableCommand che si trova nella storiaComandi, se non ci sono comandi allora non restituisce false
     * @return true se l'esecuzione è andata a buon fine altrimenti false
     * @throws ErrorLogException 
     * @throws CommandException 
     */
    protected abstract boolean undo() throws CommandException, ErrorLogException;
    // protected boolean undo() throws CommandException, ErrorLogException {
    //     if (storiaComandi.isEmpty()) return false;
    //     UndoableCommand command = storiaComandi.pop();
    //     if (command != null) {
    //         return command.undo();
    //     }
    //     return false;
    // }


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
