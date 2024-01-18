package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Client;

/**factory per la creazione di Comandi specifici per il Client
 * 
 */
public class CommandFactoryClient extends CommandFactoryI<Client> implements CommandFactory{
  
    
    public CommandFactoryClient(Client gestore) throws CommandException {
        super(gestore);

    }

    public Command getCommand(String[] params) throws CommandException {
        String scelta = params == null || params.length == 0 ? "" : params[0];
        scelta = scelta.length() > 0 && (String.valueOf(((Character)scelta.charAt(0)))).equals("$") ? "$" : scelta;
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
                    "set\t\tpermette di modificare la socket oppure il nome del client\n\t\t(set name nuovoNome) permette di cambiare il nome del client\n\t\t(set remoto nuovoIpRemoto nuovaPortaRemota) permette di cambiare a quale server collegarsi\n"+
                    "$\t\tpermette di inviare un comando al server, invia '$help' per sapere tutta la lista di comandi disponibili\n"+
                    "file\t\tpermette di selezionare o creare un file nel quale stampare le risposte del server (di default toFile è enabled append)\n\t\t(file nomeFile.estensione)crea o seleziona il file nella cartella fileUtente, se era già stato selezionato un file,\n\t\til file precedente non viene eliminato, ma viene solo selezionato o creato il nuovo file\n"+
                    "toFile\t\tpermette di attivare,disattivare e cambiare modalità di stampa su file\n\t\t(toFile enable modalità) modalità può essere append o overwrite\n\t\t(toFile disable) viene disattivata la scrittura su file"
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
                case "socket":
                case "s":
                    if(params.length == 4)return new CommandSetSocketClient(this.getGestore(),params[2],params[3]);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }
            case "$":
                return new CommandInviaMsgClient(this.concatenaParams(params, 0), getGestore());
            case "file":
            case "toFile":
            default:    
                return new CommandDefault(params);
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
