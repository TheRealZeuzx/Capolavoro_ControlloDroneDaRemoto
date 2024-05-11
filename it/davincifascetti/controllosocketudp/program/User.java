package it.davincifascetti.controllosocketudp.program;
import java.util.concurrent.atomic.AtomicBoolean;

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
            //this.registraComandiClient();
            this.registraComandiGestoreCS();
            //this.registraComandiServer();
            //this.registraComandiServerThread();
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
    //!le regex sono errate (se metto new cle funziona)
    //!per ora solo la registrazione dei comandi gestore Client Server è usata perchè non da errori, vanno terminate tutte le registrazioni e comandi
    private void registraComandiClient(){
        String path = "it.davincifascetti.controllosocketudp.command.";
        //normali
        Client.comandi.registraComando( "^\b(he?l?p?[ ]*)|[?][ ]*$",path + "CommandHelp");
        Client.comandi.registraComando( "^\bin?f?o?[ ]*$",path + "CommandHelp");
        //set
        Client.comandi.registraComando( "^\bse?t?[ ]+po?r?t?[ ]+.*$",path + "CommandSetNomeClient");
        Client.comandi.registraComando( "^\bse?t?[ ]+na?m?e?[ ]+.*$",path + "CommandSetSocketClient");

        /* 
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp(          
                    "Comandi Terminale Client\n\n"+
                    "help\t\tpermette di visualizzare tutti i comandi \n" + 
                    "quit\t\tpermette di tornare al Terminale Generale \n" + 
                    "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
                    "info\t\tpermette di visualizzare le informazioni di questo client\n" +
                    "set\t\tpermette di modificare la socket oppure il nome del client\n\t\t(set name nuovoNome) permette di cambiare il nome del client\n\t\t(set socket nuovoIpRemoto nuovaPortaRemota) permette di cambiare a quale server collegarsi\n"+
                    "$\t\tpermette di inviare un comando al server, invia '$help' per sapere tutta la lista di comandi disponibili\n"
                );
            case "info":
            case "i":
                return new CommandHelp(this.getGestore().toString());
            case "s":
            case "set":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "name":
                case "n":                      
                    if(params.length == 3)return new CommandSetNomeClient(this.getGestore(),params[2]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                case "socket":
                case "s":
                    if(params.length == 4)return new CommandSetSocketClient(this.getGestore(),params[2],params[3]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                default:
                    throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }
            case "$":
                switch (params[0]) {
                    case "$remote":
                    case "$r":
                        return new CommandTelecomando(this.getGestore());
                    case "$from":
                        if(params.length == 2) return new CommandFromFile(this.getGestore(),params[1]);
                        throw new CommandException("Errore,non è stato specificato cosa creare");
                    case "$log":
                        return new CommandInviaMsgClient(this.concatenaParams(params, 1), this.getGestore());
                    default:
                        return new CommandInviaMsgClient(this.concatenaParams(params, 0), this.getGestore());
                        
                }
            default:    
                return new CommandDefault(params);
        }
        */
    }
   
    private void registraComandiServer(){
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
    
    
    private void registraComandiGestoreCS(){
        String path = "it.davincifascetti.controllosocketudp.command.";
        GestoreClientServer.comandi.registraComando( "^\\b(h(?:e(?:l(?:p)?)?)?[ ]*)|[?][ ]*$//i",path + "CommandHelp");
        GestoreClientServer.comandi.registraComando( "^\\bi(?:n(?:f(?:o)?)?)?)?[ ]*$",path +"CommandHelp");            
        GestoreClientServer.comandi.registraComando( "n(?:ew?)?[ ]+c(?:l(?:i(?:e(?:n(?:t)?)?)?)?)?[ ]+",path +"CommandNewClient"); 
        //! di seguito il vecchio switch case                                                                 
        /* 
        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp(            
                "Comandi Terminale Generale\n\n"+
                "help\t\tpermette di visualizzare tutti i comandi \n" + 
                "quit\t\tpermette di terminare l'esecuzione \n" + 
                "show\t\tpermette di visualizzare la lista di tutti i client e server creati\n\t\t(show all) per visualizzare lista di client e server,\n\t\t(show client) per visualizzare solo la lista di client,\n\t\t(show server) per visualizzare solo la lista di server\n"+
                "info\t\tpermette di visualizzare le informazioni di uno specifico client o server (info nome)\n" +
                "new \t\tpermette di creare un server o client specifico\n\t\t(new client nomeClient ip porta) l'ip e la porta si riferiscono al socket remoto destinatario\n\t\t(new server nomeServer porta) la porta si riferisce alla porta su cui creare la nuova Socket locale\n\t\tse si specifica solo il nome(sia in client che server) sarà necessario attivarlo in seguito\n" + 
                "select\t\tpermette di selezionare un server o client in base al nome\n\t\t(select client nomeClient) permette di selezionare un client\n\t\t(select server nomeServer) permette di selezionare un server\n"+
                "delete\t\tpermette di eliminare un server o client in base al nome\n\t\t(delete client nomeClient) permette di eliminare un client\n\t\t(delete server nomeServer) permette di eliminare un server\n" +
                "undo\t\tpermette di annullare l'ultima operazione significativa eseguita (new e delete)\n"
                );
            case "sh":
            case "show":
            
                switch (params == null || params.length <= 1 ? "" : params[1]) {
                case "a":
                case "all":
                    if(params.length == 2)return new CommandShowAll(this.getGestore());
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                case "c":
                case "client":
                    if(params.length == 2)return new CommandShowClient(this.getGestore());
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                case "s":
                case "server":
                    if(params.length == 2)return new CommandShowServer(this.getGestore());
                default:
                    throw new CommandException("Errore, non è stato specificato cosa stampare");
                }
            case "i":
            case "info":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "c":
                case "client":
                    if(params.length == 3) return new CommandInfo(this.getGestore(),true,params[2]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                case "s":
                case "server":
                    if(params.length == 3) return new CommandInfo(this.getGestore(),false,params[2]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                default:
                    throw new CommandException("Errore, non è stato specificato cosa stampare");
                }
            case "new":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "c":
                case "client":
                    if(params.length == 3)return new CommandNewClient(this.getGestore(),this.getGestore().getTerminalClient(),params[2]);
                    if(params.length == 5)return new CommandNewClient(this.getGestore(),this.getGestore().getTerminalClient(),params[2],params[3],params[4]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                case "s":
                case "server":
                    if(params.length == 3)return new CommandNewServer(this.getGestore(),this.getGestore().getTerminalServer(),params[2]);
                    if(params.length == 4)return new CommandNewServer(this.getGestore(),this.getGestore().getTerminalServer(),params[2],params[3]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                default:
                    throw new CommandException("Errore, non è stato specificato cosa creare");
                }
            case "del":
            case "delete":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "c":
                case "client":
                    if(params.length == 3)return new CommandDeleteClient(this.getGestore(),params[2]);
                case "s":
                case "server":
                    if(params.length == 3)return new CommandDeleteServer(this.getGestore(),params[2]);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa eliminare");
                }
            case "sel":
            case "select":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "c":
                case "client":                      
                    if(params.length == 3)return new CommandSelectClient(this.getGestore(),params[2]);
                case "s":
                case "server":
                    if(params.length == 3)return new CommandSelectServer(this.getGestore(),params[2]);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }    
            default:    
                return new CommandDefault(params);
        }
        */
    }
    private void registraComandiServerThread(){
        String path = "it.davincifascetti.controllosocketudp.command.";

        /* 
        String scelta = params == null || params.length == 0 ? "" : params[0];
        if(scelta.isBlank())return new CommandHelp("errore, il comando '" + this.concatenaParams(params,0) +"' non è riconosciuto" ,this.gestore);
        if(String.valueOf(scelta.charAt(0)).equals("$")){
            
            switch (scelta) {
            case "$l":
            case "$log":
                if(params.length >= 2)return new CommandStampaVideoServerThread(this.concatenaParams(params, 1),this.gestore);
            case "$f":
            case "$file":
                if(params.length >= 2)return new CommandFileLog(this.concatenaParams(params, 1), this.gestore);
            case "$h":
            case "$help":
                return new CommandHelp(
                    "Comandi Remoti Disponibili\n\n"+
                    "$help\t\tpermette di visualizzare tutti i comandi \n" + 
                    "$log\t\tpermette di inviare al server un msg\n\t\t($log msgStampare) il messaggio può contenere spazi\n" + 
                    "$file\t\tpermette di stampare sul file(che prende nome del server se non è selezionato dal server) il contenuto del msg\n\t\t(il msg è stampato anche sulla console del server)\n\t\t($file msgStampare) il messaggio può comprendere spazi\n"+
                    "$remote\t\tpermette di attivare la modalità telecomando (invia un char su pressione tasti diversi)\n"+
                    "$from\t\tpermette di leggere un file locale(client) e inviarlo al server\n\t\t($file nomefile) nomefile non necesseta delle ''\t"
                ,this.gestore);
            default:
                return new CommandHelp("errore, il comando '" + this.concatenaParams(params,0) +"' non è riconosciuto" ,this.gestore);
            }
        }
        return new CommandServerDefaultResponse(this.gestore,this.concatenaParams(params,0));
        */
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
