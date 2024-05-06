package it.davincifascetti.controllosocketudp.command;
import java.util.HashMap;
import java.util.Map;

import it.davincifascetti.controllosocketudp.program.ServerThread;


/**
    CommandFactoryClient.
    Factory per la creazione di specifici comandi per il Server-Thread
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/ 
//! la sto trasformando in una factory generale , utilizzando una HashMap, la HashMap non è synchronized
//!K è il gestore, T è il tipo di comando (il gestore definisce il tipo di comando che può essere inserito)
//TODO delegare la separazione dei parametri ecc ad una classe apposita in modo da separarla dal comando che poi in base ai parametri agirà differentemente? oppure comandi diversi per parametri diversi?
//TODO quando registro il comando gli passo solo il gestore, all'esecuzione gli passero i parametri necessari.

//! la classe CommandFactoryRisposta va joinata a server
public class CommandFactoryRisposta<K extends Commandable,T extends CommandI<K>> implements CommandFactory{
    private ServerThread gestore;
    private HashMap<String,Command> arrayAssociativo = null;
    /**
        Costruttore di default di CommandFactoryRisposta.
        @param gestore è l'oggetto che farà da receiver per i comandi
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandFactoryRisposta() throws CommandException{
        this.gestore = gestore;
        this.arrayAssociativo = new HashMap<String,Command>();
        
        this.registraComando( "$l",(Command)new CommandStampaVideoServerThread());

    }

    /**
        getCommand.
        Metodo che, in base ai parametri, ritorna il comando corrispondente.
        @param params Comando da eseguire in formato testuale.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public Command getCommand(String []params) throws CommandException {
        
        String scelta = params == null || params.length == 0 ? "" : params[0];
        if(scelta.isBlank())return new CommandHelp("errore, il comando '" + this.concatenaParams(params,0) +"' non è riconosciuto" ,this.gestore);
        if(String.valueOf(scelta.charAt(0)).equals("$")){
            
            for (Map.Entry<String, Command> entry : this.arrayAssociativo.entrySet()) {
                String key = entry.getKey();
                Command value = entry.getValue();
                System.out.println("Key=" + key + ", Value=" + value);
            }

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
            msg = msg.length() == 0 ? "" : msg.substring(0,msg.length()-1);
        }
        return msg.isBlank() ? "":msg;
    }

    public void registraComando(String call,T comando){
        this.arrayAssociativo.put(call, comando);
    }
}
