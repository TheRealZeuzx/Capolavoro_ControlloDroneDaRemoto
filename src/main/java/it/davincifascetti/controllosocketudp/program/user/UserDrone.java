package it.davincifascetti.controllosocketudp.program.user;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandList;
import it.davincifascetti.controllosocketudp.program.Cli;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.Component;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.ServerThread;

import java.awt.event.KeyEvent;
/**classe User è una sorta di wrapper per Error e GestoreClientServer, in modo da evitare all'utente di occuparsi della creazione del errorLog e GestoreClientServer
 * che saranno i componenti principali
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public final class UserDrone{
    private User user;
    public UserDrone() throws CommandException{
        this.user = new User("drone");
        this.registraComandiGenerali(Cli.class);
        this.registraComandiGestoreCS(Cli.class);
        this.registraComandiClient(Cli.class);
        this.registraComandiServer(Cli.class);
        this.registraComandiServerThread(Cli.class);
    }
    
    private void registraComandiGenerali(Class<? extends Component> clazz){
        
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = this.user.getManager(clazz).getCommandList(null);
        temp.registraComando( Integer.toString(KeyEvent.VK_ESCAPE),path + "CommandTerminaTelecomando");
        temp.registraComando( Integer.toString(KeyEvent.VK_W),path + "drone.CommandSpostaDestra");
        temp.registraComando( Integer.toString(KeyEvent.VK_A),path + "drone.CommandSpostaSinistra");
        temp.registraComando( Integer.toString(KeyEvent.VK_S),path + "drone.CommandSpostaIndietro");
        temp.registraComando( Integer.toString(KeyEvent.VK_D),path + "drone.CommandSpostaAvanti");
        temp.registraComando( Integer.toString(KeyEvent.VK_E),path + "drone.CommandRuotaDestra");
        temp.registraComando( Integer.toString(KeyEvent.VK_Q),path + "drone.CommandRuotaSinistra");
        temp.registraComando( Integer.toString(KeyEvent.VK_SHIFT),path + "drone.CommandSpostaGiu");
        temp.registraComando( Integer.toString(KeyEvent.VK_SHIFT),path + "drone.CommandSpostaSu");
        temp.setStringaHelp("premi la tab per iniziare ad inviare ('escape' per uscire)");
    }

    private void registraComandiClient(Class<? extends Component> clazz){
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = this.user.getManager(clazz).getCommandList(Client.class);
        //normali
        temp.setStringaHelp(
            "Comandi Terminale Drone\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di tornare al Terminale Generale \n" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
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
   
    private void registraComandiServer(Class<? extends Component> clazz){
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp =  this.user.getManager(clazz).getCommandList(Server.class);
        temp.setStringaHelp(
            "Comandi Terminale InfoDrone\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di tornare al Terminale Generale \n" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
            "file\t\tpermette di abilitare la stampa su file in maniera automatica di tutto ciò che viene inviato al server\n\t\t(file nomefile modalità) se si vuole stampare sul file che prende il nome di questo server , si usa 'this' al posto del nomeFile \n\t\tla modalità può essere append oppure overwrite\n\t\t(file disable) permette di disabilitare la stampa su file, una volta disabilitata,\n\t\tsarà necessario usare il comando (file nomefile modalita) per riattivarla\n"
        );
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");
        temp.registraComando( "^[?][ ]*$",path + "CommandHelp");
        temp.registraComando( "f(?:i(?:l(?:e)?)?)?[ ]+",path + "CommandEnableToFile");
        temp.registraComando( "^f(?:i(?:l(?:e)?)?)?[ ]+d(?:i(?:s(?:a(?:b(?:l(?:e)?)?)?)?)?)?[ ]*$",path + "CommandDisableToFile");
    }
    
    
    private void registraComandiGestoreCS(Class<? extends Component> clazz){
        //!fatto
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp =  this.user.getManager(clazz).getCommandList(GestoreClientServer.class);
        temp.setStringaHelp(
            "Comandi Terminale Generale\n\n"+
            "help\t\tpermette di visualizzare tutti i comandi \n" + 
            "quit\t\tpermette di terminare l'esecuzione \n" + 
            "show all\t\tpermette di visualizzare le informazioni della connessione\n" + //stato: connesso    video: attivo    stats: attivo
            "connect\t\tpermette di connettersi al drone\n" + 
            "status\t\tpermette di visualizzare lo stato del drone\n" + 
            "pilot\t\tpermette di inviare msg al drone o attivare il remote\n" + 
            "undo\t\tpermette di annullare l'ultima operazione significativa eseguita\n"
        );
        //TODO scegliere se far in modo da creare una finestra separata per la visualizzazione delle stats del drone
        temp.registraComando( "^(h(?:e(?:l(?:p)?)?)?[ ]*)$",path + "CommandHelp");
        temp.registraComando( "^[?][ ]*$",path + "CommandHelp");
        //!preferisco levarlo , uso pilot e gli mando il comando "command" se non è connesso (forse lo fa anche la classe WifiDrone) 
        //temp.registraComando( "c(?:o(?:n(?:n(?:e(?:c(?:t)?)?)?)?)?)?[ ]*",path +"drone.CommandConnectDrone"); 
        temp.registraComando( "sh(?:o(?:w)?)?[ ]+",path +"CommandShow");
        temp.registraComando( "^st(?:a(?:t(?:u(?:s)?)?)?)?[ ]*$",path +"drone.CommandStatus"); //seleziona il server status
        temp.registraComando( "p(?:i(?:l(?:o(?:t)?)?)?)?[ ]*",path +"drone.CommandPilot"); //seleziona il client 
        
    }
    private void registraComandiServerThread(Class<? extends Component> clazz){
        //!non deve fare niente se non stampare
        String path = "it.davincifascetti.controllosocketudp.command.";
        CommandList temp = this.user.getManager(clazz).getCommandList(ServerThread.class);
        //comando default
        temp.registraComando( null,path + "CommandStampaVideoServerThread",true);
        
    }
    public User getUser(){return this.user;}

}
