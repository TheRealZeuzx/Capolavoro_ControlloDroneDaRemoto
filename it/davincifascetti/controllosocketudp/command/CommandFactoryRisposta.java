package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.ServerThread;


/**
    CommandFactoryClient.
    Factory per la creazione di specifici comandi per il Server-Thread
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/ 
public class CommandFactoryRisposta implements CommandFactory{
    private ServerThread gestore;
    /**
        Costruttore di default di CommandFactoryRisposta.
        @param gestore è l'oggetto che farà da receiver per i comandi
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandFactoryRisposta(ServerThread gestore) throws CommandException{
        this.gestore = gestore;
    }

    /**
        getCommand.
        Metodo che, in base ai parametri, ritorna il comando corrispondente.
        @param params Comando da eseguire in formato testuale.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public Command getCommand(String []params) throws CommandException {
        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch(scelta){
            
            case "$l":
            case "$log"://TODo per stampare a video tutti i msg la parte client deve creare un loop che prende i msg e ci mette $log davanti
                if(params.length >= 2)return new CommandStampaVideoServerThread(this.concatenaParams(params, 1),this.gestore);
            case "$f":
            case "$file": //TODO con questo comando prendo quello che sta dopo file nomeFile e lo stampo sul file selezionato
                if(params.length >= 2)return new CommandFileLog(this.concatenaParams(params, 1), this.gestore);
            case "$r":
            case "$remote": 
                return new CommandHelp(this.concatenaParams(params, 1), this.gestore);
            case "$h":
            case "$help":
                return new CommandHelp(
                    "Comandi Remoti Disponibili\n\n"+
                    "$help\t\tpermette di visualizzare tutti i comandi \n" + 
                    "$undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
                    "$log\t\tpermette di stampare sulla console del server un msg\n\t\t($log msgStampare) il messaggio può contenere spazi\n" + 
                    "$file\t\tpermette di stampare sul file(che prende nome del server) il contenuto del msg\n\t\t($file msgStampare) il messaggio può comprendere spazi\n"
                ,this.gestore);
            default: //TODO fare un comando per invio ?
                return new CommandHelp("errore, il comando '" + this.concatenaParams(params,0) +"' non è riconosciuto" ,this.gestore);
        }
        
    }

    /**
        concatenaParams.
        Metodo privato per concatenare i parametri. 
        Di solito utilizzato per separare i parametri del comando dalla keyword. 
    */
    private String concatenaParams(String[] params,int startIndex){
        String msg ="";
        if(params != null && params.length != 0){
            for(int i = startIndex;i<params.length;i++){
                msg += params[i] + " ";
            }
            msg = msg.substring(0,msg.length()-1);
        }
        return msg.isBlank() ? "":msg;
    }
}
