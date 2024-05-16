package it.davincifascetti.controllosocketudp.program;
import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandList;
import it.davincifascetti.controllosocketudp.command.Commandable;
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
    private static final AtomicBoolean inizialized = new AtomicBoolean();
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
        this.init();
        this.gestore = new GestoreClientServer(this.errorLog);
    }
    
    private void init(){
        if(User.inizialized.compareAndSet(false, true)){
            this.registraComandiGestoreCS();
            this.registraComandiClient();
            this.registraComandiServer();
            this.registraComandiServerThread();
        }
    }

    /**avvia il terminale di gestoreclientserver di conseguenza il programma in se
     * 
     * @throws CommandException
     */
    public void start() throws CommandException{
        this.gestore.startTerminal();
    }

    //TODO Divisione dei comandi in pacchetti dedicati  
    //TODO fixare ServerThread e capire se funziona normalmente cambiando la factory o se ci sono problemi
    //TODO fixare i comandi CommandHelp in modo da renderli utilizzabili (paramtri giusti gestore.class(),String)
    //TODO fixare i comandi che non funzionano (risolvere problemi riguardanti switch interni ecc)
    //TODO possibilmente fare le registrazioni prendendo i file da xml, decidere se farlo o no
    //TODO controllo e nel caso riscrittura concorrenza ErrorLogger/FileLogger
    //TODO Fixare le regex
    //TODO switch a xml per i comandi e regex vv
    //!la lista è unica per tutti i commandable, deve essere univoca per ognuno!
    //!per ora solo la registrazione dei comandi gestore Client Server è usata perchè non da errori, vanno terminate tutte le registrazioni e comandi
    private void registraComandiClient(){
        System.out.println("Debug: | Registrazione comandi Client |");
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = Commandable.ListeComandi.getCommandList(Client.class);
        //normali
        temp.setStringaHelp(
            "Comandi Terminale Client\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di tornare al Terminale Generale \n" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
            "info\t\tpermette di visualizzare le informazioni di questo client\n" +
            "set\t\tpermette di modificare la socket oppure il nome del client\n\t\t(set name nuovoNome) permette di cambiare il nome del client\n\t\t(set socket nuovoIpRemoto nuovaPortaRemota) permette di cambiare a quale server collegarsi\n"+
            "$\t\tpermette di inviare un comando al server, invia '$help' per sapere tutta la lista di comandi disponibili\n"
        );
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");
        temp.registraComando( "\\$",path + "CommandInviaMsgClient");//qualsiasi cosa scritta dopo '$' verrà inviata al server
        temp.registraComando( "(f(?:r(?:o(?:m)?)?)?[ ]*)",path + "CommandFromFile");
        temp.registraComando( "^(r(?:e(?:m(?:o(?:t(?:e)?)?)?)?)?[ ]*)$",path + "CommandTelecomando");
        //!non funzionanti
        temp.registraComando( "^i(?:n(?:f(?:o)?)?)?[ ]*)$",path + "CommandInfoClient");
        temp.registraComando( "s(?:e(?:t?)?)?[ ]+p(?:o(?:r(?:t)?)?)?[ ]+",path + "CommandSetSocketClient");
        temp.registraComando( "s(?:e(?:t?)?)?[ ]+n(?:a(?:m(?:e)?)?)?[ ]+",path + "CommandSetNomeClient");
    }
   
    private void registraComandiServer(){
        System.out.println("Debug: | Registrazione comandi Server |");
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = Commandable.ListeComandi.getCommandList(Server.class);
        temp.setStringaHelp(
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
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");


        //normali
        // Commandable.ListeComandi.getCommandList(Server.class).registraComando( "^\b(he?l?p?[ ]*)|[?][ ]*$",path + "CommandHelp");
        // Commandable.ListeComandi.getCommandList(Server.class).registraComando( "^\bin?f?o?[ ]*$",path + "CommandHelp");
        // //set
        // Commandable.ListeComandi.getCommandList(Server.class).registraComando( "^\bse?t?[ ]+po?r?t?[ ]+.*$",path + "CommandSetNomeServer");
        // Commandable.ListeComandi.getCommandList(Server.class).registraComando( "^\bse?t?[ ]+na?m?e?[ ]+.*$",path + "CommandSetSocketServer");
        // //$
        // Commandable.ListeComandi.getCommandList(Server.class).registraComando( "^\b\\$lo?g?[ ]*$",path + "CommandStampaVideoServerThread");
                /* 
        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {

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
    
    
    private void registraComandiGestoreCS(){
        //!fatto
        System.out.println("Debug: | Registrazione comandi GestoreClientServer |");
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = Commandable.ListeComandi.getCommandList(GestoreClientServer.class);
        temp.setStringaHelp(
            "Comandi Terminale Generale\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di terminare l'esecuzione \n" + 
            "show\t\tpermette di visualizzare la lista di tutti i client e server creati\n\t\t(show all) per visualizzare lista di client e server,\n\t\t(show client) per visualizzare solo la lista di client,\n\t\t(show server) per visualizzare solo la lista di server\n"+
            "info\t\tpermette di visualizzare le informazioni di uno specifico client o server (info (client | server) nome)\n" +
            "new \t\tpermette di creare un server o client specifico\n\t\t(new client nomeClient ip porta) l'ip e la porta si riferiscono al socket remoto destinatario\n\t\t(new server nomeServer porta) la porta si riferisce alla porta su cui creare la nuova Socket locale\n\t\tse si specifica solo il nome(sia in client che server) sarà necessario attivarlo in seguito\n" + 
            "select\t\tpermette di selezionare un server o client in base al nome\n\t\t(select client nomeClient) permette di selezionare un client\n\t\t(select server nomeServer) permette di selezionare un server\n"+
            "delete\t\tpermette di eliminare un server o client in base al nome\n\t\t(delete client nomeClient) permette di eliminare un client\n\t\t(delete server nomeServer) permette di eliminare un server\n" +
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita (new e delete)\n"
        );
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");
        temp.registraComando( "^[?][ ]*$",path + "CommandHelp");
        temp.registraComando( "se(?:l(?:e(?:c(?:t)?)?)?)?[ ]+c(?:l(?:i(?:e(?:n(?:t)?)?)?)?)?[ ]+",path +"CommandSelectClient");
        temp.registraComando( "se(?:l(?:e(?:c(?:t)?)?)?)?[ ]+s(?:e(?:r(?:v(?:e(?:r)?)?)?)?)?[ ]+",path +"CommandSelectServer");
        temp.registraComando( "n(?:ew?)?[ ]+c(?:l(?:i(?:e(?:n(?:t)?)?)?)?)?[ ]+",path +"CommandNewClient"); //regex brutta da vedere e probabilmente slow as hell ma funziona
        temp.registraComando( "n(?:ew?)?[ ]+s(?:e(?:r(?:v(?:e(?:r)?)?)?)?)?[ ]+",path +"CommandNewServer");
        temp.registraComando( "d(?:e(?:l(?:e(?:t(?:e)?)?)?)?)?[ ]+c(?:l(?:i(?:e(?:n(?:t)?)?)?)?)?[ ]+",path +"CommandDeleteClient");
        temp.registraComando( "d(?:e(?:l(?:e(?:t(?:e)?)?)?)?)?[ ]+s(?:e(?:r(?:v(?:e(?:r)?)?)?)?)?[ ]+",path +"CommandDeleteServer");
        temp.registraComando( "sh(?:o(?:w)?)?[ ]+",path +"CommandShow");
        temp.registraComando( "i(?:n(?:f(?:o)?)?)?[ ]+",path +"CommandInfo");

    }
    private void registraComandiServerThread(){
        System.out.println("Debug: | Registrazione comandi ServerThread |");
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = Commandable.ListeComandi.getCommandList(ServerThread.class);
        temp.setStringaHelp(
            "Comandi Remoti Disponibili\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "print\t\tpermette di inviare al server un msg\n\t\t(print msgStampare) il messaggio può contenere spazi\n" + 
            "file\t\tpermette di stampare sul file(che prende nome del server se non è selezionato dal server) il contenuto del msg\n\t\t(il msg è stampato anche sulla console del server)\n\t\t($file msgStampare) il messaggio può comprendere spazi\n"
        );
        //risponde ai comandi come fossero comandi locali del client o server ecc, quindi se scrivo help manda msh help, se voglio stampare scriverò print msgDaStampare
        temp.registraComando( "^f(?:i(?:l(?:e)?)?)?[ ]*$",path + "CommandFileLog");
        temp.registraComando( "^h(?:e(?:l(?:p)?)?)?[ ]*$",path + "CommandInviaHelpToClient");
        temp.registraComando( "p(?:r(?:i(?:n(?:t)?)?)?)?[ ]+",path + "CommandStampaVideoServerThread");
        //comando default
        temp.registraComando( null,path + "CommandInviaMsgDefaultToClient",true);
        
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
}
