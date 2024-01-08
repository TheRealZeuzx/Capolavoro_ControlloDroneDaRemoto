/**classe User è una sorta di wrapper per Error e GestoreClientServer, in modo da evitare all'utente di occuparsi della creazione del errorLog e GestoreClientServer
 * che saranno i componenti principali
 * 
 */
public class User {
    private Error errorLog;
    private GestoreClientServer gestore;
    public User(String pathErrorLogFile){
        this.errorLog = new Error(pathErrorLogFile);
        this.gestore = new GestoreClientServer(this.errorLog);
    }
    
    public void start(){
        this.gestore.startTerminal();
    }


    //! MAIN
    public static void main(String[] args) {
        User u = new User("errorLog.txt");
        u.start();
    }
}
