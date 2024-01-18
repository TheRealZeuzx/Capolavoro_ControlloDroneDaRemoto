package it.davincifascetti.controllosocketudp.program;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;

/**classe User è una sorta di wrapper per Error e GestoreClientServer, in modo da evitare all'utente di occuparsi della creazione del errorLog e GestoreClientServer
 * che saranno i componenti principali
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class User {
    private ErrorLog errorLog;
    private GestoreClientServer gestore;
    /**
     * 
     * @param pathErrorLogFile path del file errori
     * @throws CommandException
     */
    public User(String pathErrorLogFile) throws CommandException{
        this.errorLog = new ErrorLog(pathErrorLogFile);
        this.gestore = new GestoreClientServer(this.errorLog);
    }
    
    /**avvia il terminale di gestoreclientserver di conseguenza il programma in se
     * 
     * @throws CommandException
     */
    public void start() throws CommandException{
        this.gestore.startTerminal();
    }


    //! MAIN
    public static void main(String[] args) {
        /* msg utili per testing
        * new c c1 localhost 1212
        * new s s1 1212
        * select c c1
        */
        User u;
        try {
            u = new User("errorLog.txt");
            System.out.println("User creato correttamente");
            u.start();
        } catch (CommandException e) {
            e.getMessage();
        }
       
    }
}
