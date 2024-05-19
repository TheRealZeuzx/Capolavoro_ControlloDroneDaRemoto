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
public final class UserDrone extends User{

    public UserDrone(String pathErrorLogFile) throws CommandException{
        super(pathErrorLogFile, UserDrone.class);
    }

    protected void registraComandiClient(){
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp =  User.getManager(UserDrone.class).getCommandList(Client.class);
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
   
    protected void registraComandiServer(){
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp =  User.getManager(UserDrone.class).getCommandList(Server.class);
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
    }
    
    
    protected void registraComandiGestoreCS(){
        //!fatto
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp =  User.getManager(UserDrone.class).getCommandList(GestoreClientServer.class);
        temp.setStringaHelp(
            "Comandi Terminale Generale\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di terminare l'esecuzione \n" + 
            "show\t\tpermette di visualizzare la lista di tutti i client e server creati\n\t\t(show all) per visualizzare lista di client e server,\n\t\t(show client) per visualizzare solo la lista di client,\n\t\t(show server) per visualizzare solo la lista di server\n"+
            "info\t\tpermette di visualizzare le informazioni di uno specifico client o server (info (client | server) nome)\n" +
            "connect\t\tpermette di connettersi al drone\n\t\t(new client nomeClient ip porta) l'ip e la porta si riferiscono al socket remoto destinatario\n\t\t(new server nomeServer porta) la porta si riferisce alla porta su cui creare la nuova Socket locale\n\t\tse si specifica solo il nome(sia in client che server) sarà necessario attivarlo in seguito\n" + 
            "select\t\tpermette di selezionare un server o client in base al nome\n\t\t(select client nomeClient) permette di selezionare un client\n\t\t(select server nomeServer) permette di selezionare un server\n"+
            "delete\t\tpermette di eliminare un server o client in base al nome\n\t\t(delete client nomeClient) permette di eliminare un client\n\t\t(delete server nomeServer) permette di eliminare un server\n" +
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita (new e delete)\n"
        );
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");
        temp.registraComando( "^[?][ ]*$",path + "CommandHelp");
        temp.registraComando( "connect",path + "CommandConnect"); 
        // Comando per inizializzare il client del drone (Si connette all'ip 192.168.10.1 porta 8889 e manda un pacchetto contenente "command") 
        // new CommandNewClient(drone1, 192.168.10.1, 8889)
        // new CommandInviaMsgClient("command") 
        // new CommandNewServer(drone1, 0.0.0.0, 8890)
        // new CommandNewServer(drone1_video, 0.0.0.0, 11111)
        temp.registraComando( "status",path + "CommandStatus");
        // DA FARE DENTRO REGISTRACLIENT vv
        temp.registraComando( "video[ ]+",path + "CommandGetVideo");
        // drone1.CommandInviaMsgClient("streamon") 
        // drone1.CommandInviaMsgClient("streamoff") 
        
    }
    protected void registraComandiServerThread(){
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = User.getManager(UserDrone.class).getCommandList(ServerThread.class);
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

    @Override
    protected void logicaStart() throws CommandException {
        this.getGestore().startTerminal();
    }

}
