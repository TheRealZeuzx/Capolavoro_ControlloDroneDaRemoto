/**classe User è una sorta di wrapper per Error e GestoreClientServer, in modo da evitare all'utente di occuparsi della creazione del errorLog e GestoreClientServer
 * che saranno i componenti principali
 * 
 */
public class User {
    private ErrorLog errorLog;
    private GestoreClientServer gestore;
    public User(String pathErrorLogFile) throws CommandException{
        this.errorLog = new ErrorLog(pathErrorLogFile);
        this.gestore = new GestoreClientServer(this.errorLog);
    }
    
    public void start(){
        this.gestore.startTerminal();
    }


    //! MAIN
    public static void main(String[] args) {
        User u;
        try {
            u = new User("errorLog.txt");
            u.start();
        } catch (CommandException e) {
            e.getMessage();
        }
       
    }
}
