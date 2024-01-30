package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Client;

/**
    CommandFactoryClient.
    Factory per la creazione di comandi specifici per il client.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/ 
public class CommandFactoryClient extends CommandFactoryI<Client> implements CommandFactory{
  
    /**
        Costruttore di default di CommandFactoryClient.
        @param gestore è l'oggetto che farà da receiver per i comandi
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandFactoryClient(Client gestore) throws CommandException {
        super(gestore);

    }

    /**
        getCommand.
        Metodo che, in base ai parametri, ritorna il comando corrispondente.
        @param params Comando da eseguire in formato testuale.
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
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
    
}
