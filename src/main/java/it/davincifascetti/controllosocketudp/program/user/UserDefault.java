package it.davincifascetti.controllosocketudp.program.user;


import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandList;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.ServerThread;


/**classe User è una sorta di wrapper per Error e GestoreClientServer, in modo da evitare all'utente di occuparsi della creazione del errorLog e GestoreClientServer
 * che saranno i componenti principali
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public final class UserDefault extends User{
    
    /**
     * @param pathErrorLogFile path del file errori
     * @throws CommandException
     */
    public UserDefault() throws CommandException{
        super(UserDefault.class);
    }

    //TODO Divisione dei comandi in pacchetti dedicati ?
    //TODO possibilmente fare le registrazioni prendendo i file da xml, decidere se farlo o no
    //TODO controllo e nel caso riscrittura concorrenza ErrorLogger/FileLogger
    
    //quella sotto non è un problema degli spazi, è problema del metodo removeClient di gestoreClientServer e del comando DeleteClient/DeleteServer, 
    //semplicemente la ricerca restituiva null e nel metodo removeClient il caso null non era considerato... , fixato adesso il caso null è gestito e 
    //quando faccio la ricerca, nel comando se restiuisce null faccio una throw.      
    // // FRA......... Dobbiamo gestire gli spazi dopo il parametro. Ho appena ricevuto una nullpointerexception dal delete :sob:
    // // ! TO RECREATE: "new c c1 " "del c c1 "
    // // ! ALTRO ERRORE: comando non esistente in client = exception
    // // ! TO RECREATE: ">new c c1 localhost 1111 " ">se c c1" ">rem"
    protected void registraComandiClient(){
        
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = this.getManager().getCommandList(Client.class);
        //normali
        temp.setStringaHelp(
            "Comandi Terminale Client\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di tornare al Terminale Generale \n" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
            "info\t\tpermette di visualizzare le informazioni di questo client\n" +
            "set\t\tpermette di modificare la socket oppure il nome del client\n\t\t(set name nuovoNome) permette di cambiare il nome del client\n\t\t(set socket nuovoIpRemoto nuovaPortaRemota) permette di cambiare a quale server collegarsi\n"+
            "from\t\tserve a prendere l'input da spedire da un file(from nomeFile)\n"+
            "remote\t\tattiva la modalità telecomando ('e' per uscire)\n"+
            "$\t\tpermette di inviare un comando al server, invia '$help' per sapere tutta la lista di comandi disponibili\n"
        );
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");
        temp.registraComando( "^[?][ ]*$",path + "CommandHelp");
        temp.registraComando( "\\$",path + "CommandInviaMsgClient");//qualsiasi cosa scritta dopo '$' verrà inviata al server
        temp.registraComando( "(f(?:r(?:o(?:m)?)?)?[ ]+)",path + "CommandFromFile");
        temp.registraComando( "^(r(?:e(?:m(?:o(?:t(?:e)?)?)?)?)?[ ]*)$",path + "CommandTelecomando");
        temp.registraComando( "^i(?:n(?:f(?:o)?)?)?[ ]*$",path + "CommandToString");
        temp.registraComando( "s(?:e(?:t?)?)?[ ]+s(?:o(?:c(?:k(?:e(?:t)?)?)?)?)?[ ]+",path + "CommandSetSocketClient");
        temp.registraComando( "s(?:e(?:t?)?)?[ ]+n(?:a(?:m(?:e)?)?)?[ ]+",path + "CommandSetNomeClient");
    }
   
    protected void registraComandiServer(){
        
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = this.getManager().getCommandList(Server.class);
        temp.setStringaHelp(
            "Comandi Terminale Server\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di tornare al Terminale Generale \n" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
            "received\t\tpermette di visualizzare i messaggi ricevuti da questo server\n" +
            "info\t\tpermette di visualizzare le informazioni di questo server\n" +
            "enable\t\tpermette di avviare questo server\n" +
            "disable\t\tpermette di disattivare questo server\n" +
            "set\t\tpermette di modificare la socket oppure il nome del server\n\t\t(set name nuovoNome) permette di cambiare il nome del server\n\t\t(set socket nuovoIp nuovaPorta) permette di cambiare la socket del server\n"+
            "file\t\tpermette di abilitare la stampa su file in maniera automatica di tutto ciò che viene inviato al server\n\t\t(file nomefile modalità) se si vuole stampare sul file che prende il nome di questo server , si usa 'this' al posto del nomeFile \n\t\tla modalità può essere append oppure overwrite\n\t\t(file disable) permette di disabilitare la stampa su file, una volta disabilitata,\n\t\tsarà necessario usare il comando (file nomefile modalita) per riattivarla\n"
        );
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");
        temp.registraComando( "^[?][ ]*$",path + "CommandHelp");
        temp.registraComando( "^d(?:i(?:s(?:a(?:b(?:l(?:e)?)?)?)?)?)?[ ]*$",path + "CommandDisattivaServer");
        temp.registraComando( "^r(?:e(?:c(?:e(?:i(?:v(?:e(?:d)?)?)?)?)?)?)?[ ]*$",path + "CommandStampaMsgRicevuti");
        temp.registraComando( "^e(?:n(?:a(?:b(?:l(?:e)?)?)?)?)?[ ]*$",path + "CommandAttivaServer");
        temp.registraComando( "^i(?:n(?:f(?:o)?)?)?[ ]*$",path + "CommandToString");
        temp.registraComando( "s(?:e(?:t?)?)?[ ]+s(?:o(?:c(?:k(?:e(?:t)?)?)?)?)?[ ]+",path + "CommandSetSocketServer");
        temp.registraComando( "s(?:e(?:t?)?)?[ ]+n(?:a(?:m(?:e)?)?)?[ ]+",path + "CommandSetNomeServer");
        temp.registraComando( "f(?:i(?:l(?:e)?)?)?[ ]+",path + "CommandEnableToFile");
        temp.registraComando( "^f(?:i(?:l(?:e)?)?)?[ ]+d(?:i(?:s(?:a(?:b(?:l(?:e)?)?)?)?)?)?[ ]*$",path + "CommandDisableToFile");
    }
    
    
    protected void registraComandiGestoreCS(){

        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = this.getManager().getCommandList(GestoreClientServer.class);
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
    protected void registraComandiServerThread(){
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = this.getManager().getCommandList(ServerThread.class);
        temp.setStringaHelp(
            "Comandi Remoti Disponibili\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "print\t\tpermette di inviare al server un msg\n\t\t(print msgStampare) il messaggio può contenere spazi\n" + 
            "file\t\tpermette di stampare sul file(che prende nome del server se non è selezionato dal server) il contenuto del msg\n\t\t(il msg è stampato anche sulla console del server)\n\t\t($file msgStampare) il messaggio può comprendere spazi\n"
        );
        //risponde ai comandi come fossero comandi locali del client o server ecc, quindi se scrivo help manda msh help, se voglio stampare scriverò print msgDaStampare
        temp.registraComando( "f(?:i(?:l(?:e)?)?)?[ ]+",path + "CommandFileLog");
        temp.registraComando( "^h(?:e(?:l(?:p)?)?)?[ ]*$",path + "CommandInviaHelpToClient");
        temp.registraComando( "^[?][ ]*$",path + "CommandInviaHelpToClient");
        temp.registraComando( "p(?:r(?:i(?:n(?:t)?)?)?)?[ ]+",path + "CommandStampaVideoServerThread");
        //comando default
        temp.registraComando( null,path + "CommandInviaMsgDefaultToClient",true);
        
    }

 


}
