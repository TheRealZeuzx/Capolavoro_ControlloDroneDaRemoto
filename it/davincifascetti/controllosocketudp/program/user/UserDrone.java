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
        CommandList temp = User.getManager(UserDefault.class).getCommandList(Client.class);
        //normali
        temp.setStringaHelp(
            "Comandi Terminale Drone\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di tornare al Terminale Generale \n" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
            "info\t\tpermette di visualizzare le informazioni di questo client\n" +
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
    }
   
    protected void registraComandiServer(){
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp =  User.getManager(UserDrone.class).getCommandList(Server.class);
        temp.setStringaHelp(
            "Comandi Terminale InfoDrone\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di tornare al Terminale Generale \n" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
            "info\t\tpermette di visualizzare le informazioni di questo server\n" +
            "enable\t\tpermette di avviare questo server\n" +
            "disable\t\tpermette di disattivare questo server\n"+
            "file\t\tpermette di abilitare la stampa su file in maniera automatica di tutto ciò che viene inviato al server\n\t\t(file nomefile modalità) se si vuole stampare sul file che prende il nome di questo server , si usa 'this' al posto del nomeFile \n\t\tla modalità può essere append oppure overwrite\n\t\t(file disable) permette di disabilitare la stampa su file, una volta disabilitata,\n\t\tsarà necessario usare il comando (file nomefile modalita) per riattivarla\n"
        );
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");
        temp.registraComando( "^[?][ ]*$",path + "CommandHelp");
        temp.registraComando( "^d(?:i(?:s(?:a(?:b(?:l(?:e)?)?)?)?)?)?[ ]*$",path + "CommandDisattivaServer");
        temp.registraComando( "^e(?:n(?:a(?:b(?:l(?:e)?)?)?)?)?[ ]*$",path + "CommandAttivaServer");
        temp.registraComando( "^i(?:n(?:f(?:o)?)?)?[ ]*$",path + "CommandToString");
        temp.registraComando( "f(?:i(?:l(?:e)?)?)?[ ]+",path + "CommandEnableToFile");
        temp.registraComando( "^f(?:i(?:l(?:e)?)?)?[ ]+d(?:i(?:s(?:a(?:b(?:l(?:e)?)?)?)?)?)?[ ]*$",path + "CommandDisableToFile");
    }
    
    
    protected void registraComandiGestoreCS(){
        //!fatto
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp =  User.getManager(UserDrone.class).getCommandList(GestoreClientServer.class);
        temp.setStringaHelp(
            "Comandi Terminale Generale\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di terminare l'esecuzione \n" + 
            "info\t\tpermette di visualizzare le informazioni del drone\n" + //stato: connesso    video: attivo    stats: attivo
            "connect\t\tpermette di connettersi al drone\n" + 
            "disconnect\t\tpermette di disconnetersi al drone\n" + 
            "select\t\tpermette di passare alla vista per comandare il drone oppure alla vista per visualizzare le informazioni del drone\n" + 
            "video\t\tpermette di attivare la visualizzazione fpv del drone, usare (enable | disable)" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita (new e delete)\n"
        );
        //TODO scegliere se far in modo da creare una finestra separata per la visualizzazione delle stats del drone
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
