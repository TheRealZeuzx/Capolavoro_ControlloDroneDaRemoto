package it.davincifascetti.controllosocketudp.program;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandableException;
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
        try {
            this.errorLog = new ErrorLog(pathErrorLogFile);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
        this.registraComandiClient();
        this.registraComandiGestoreCS();
        this.registraComandiServer();
        this.registraComandiGestoreServerThread();
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
            System.out.println(e.getMessage());
        }
        System.out.println("Programma Terminato");
       
    }

    //!farlo da XML ?
    private void registraComandiClient() throws CommandException{
        String path = "it.davincifascetti.controllosocketudp.command.";
        //normali
        Client.comandi.registraComando( "^\b(he?l?p?[ ]*)|[?][ ]*$",path + "CommandHelp");
        Client.comandi.registraComando( "^\bin?f?o?[ ]*$",path + "CommandHelp");
        //set
        Client.comandi.registraComando( "^\bse?t?[ ]+po?r?t?[ ]+.*$",path + "CommandSetNomeClient");
        Client.comandi.registraComando( "^\bse?t?[ ]+na?m?e?[ ]+.*$",path + "CommandSetSocketClient");
    }
   
    private void registraComandiServer() throws CommandException{
        String path = "it.davincifascetti.controllosocketudp.command.";
        //normali
        Server.comandi.registraComando( "^\b(he?l?p?[ ]*)|[?][ ]*$",path + "CommandHelp");
        Server.comandi.registraComando( "^\bin?f?o?[ ]*$",path + "CommandHelp");
        //set
        Server.comandi.registraComando( "^\bse?t?[ ]+po?r?t?[ ]+.*$",path + "CommandSetNomeServer");
        Server.comandi.registraComando( "^\bse?t?[ ]+na?m?e?[ ]+.*$",path + "CommandSetSocketServer");
        //$
        Server.comandi.registraComando( "^\b\\$lo?g?[ ]*$",path + "CommandStampaVideoServerThread");
                /* 
        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp(          
                    "Comandi Terminale Server\n\n"+
                    "help\t\tpermette di visualizzare tutti i comandi \n" + 
                    "quit\t\tpermette di tornare al Terminale Generale \n" + 
                    "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
                    "info\t\tpermette di visualizzare le informazioni di questo server\n" +
                    "enable\t\tpermette di avviare questo server\n" +
                    "disable\t\tpermette di disattivare questo server\n" +
                    "set\t\tpermette di modificare la socket oppure il nome del server\n\t\t(set name nuovoNome) permette di cambiare il nome del server\n\t\t(set port nuovaPorta) permette di cambiare la porta del server\n"+
                    "file\t\tpermette di abilitare la stampa su file in maniera automatica di tutto ciò che viene inviato al server\n\t\t(file nomefile modalità) se si vuole stampare sul file che prende il nome di questo server , si usa 'this' al posto del nomeFile \n\t\tla modalità può essere append oppure overwrite\n\t\t(file disable) permette di disabilitare la stampa su file, una volta disabilitata,\n\t\tsarà necessario usare il comando (file nomefile modalita) per riattivarla\n"
                );
            case "info":
            case "i":
                return new CommandHelp(this.getGestore().toString());
            case "s":
            case "set":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "name":
                case "n":                      
                    if(params.length == 3)return new CommandSetNomeServer(this.getGestore(),params[2]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                case "port":
                case "p":
                    if(params.length == 3)return new CommandSetSocketServer(this.getGestore(),params[2]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                default:
                    throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }
            case "en":
            case "enable": 
                return new CommandAttivaServer(this.getGestore());
            case "dis":
            case "disable": 
                return new CommandDisattivaServer(this.getGestore());
            case "file":
            case "f":
                if(params == null || params.length <= 1 || params.length > 3) throw new CommandException("Errore, non è stato specificato cosa selezionare");
                if(params.length == 2){
                    switch(params[1]){
                        case "dis":
                        case "disable":
                            return new CommandDisableToFile(this.getGestore());
                        default:  
                            throw new CommandException("Errore, non è stato specificato cosa selezionare");
                    }
                }
                switch(params[2]){
                    case "append":
                    case "a":
                        return new CommandEnableToFile(this.getGestore(),params[1],true);
                    case "overwrite":
                    case "o":
                        return new CommandEnableToFile(this.getGestore(),params[1],false);
                    default:  
                        throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }
                
            default:    
                return new CommandDefault(params);
        }
        */
    }
    
    //!possibilmente impostarlo da file xml?
    //TODO fixare i comandi CommandHelp in modo da renderli utilizzabili (paramtri giusti gestore.class(),String)
    private void registraComandiGestoreCS() throws CommandException{
        String path = "it.davincifascetti.controllosocketudp.command.";
        //!le regex sono errate (se metto new cle funziona)
        GestoreClientServer.comandi.registraComando( "^\\b(he?l?p?[ ]*)|[?][ ]*$//i",path + "CommandHelp");
        GestoreClientServer.comandi.registraComando( "^\\bin?f?o?[ ]*$",path +"CommandHelp");
        GestoreClientServer.comandi.registraComando( "ne?w?[ ]+cl?i?e?n?t?[ ]+",path +"CommandNewClient");
    }
    private void registraComandiGestoreServerThread() throws CommandException{
        String path = "it.davincifascetti.controllosocketudp.command.";

    }
}
