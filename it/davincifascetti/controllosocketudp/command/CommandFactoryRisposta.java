package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.ServerThread;

/**Factory per la creazione di specifici comandi per il Server

 * 
 */
public class CommandFactoryRisposta implements CommandFactory{
    private ServerThread gestore;
    public CommandFactoryRisposta(ServerThread gestore) throws CommandException{
        this.gestore = gestore;
    }
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

            case "$h":
            case "$help":
                return new CommandHelp(
                    "-------------------------------------------------------------------------------\n"+
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
