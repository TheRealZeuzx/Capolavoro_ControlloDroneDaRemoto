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
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp(          
                    "-------------------------------------------------------------------------------\n"+
                    "Comandi Terminale Client\n\n"+
                    "help\t\tpermette di visualizzare tutti i comandi \n" + 
                    "quit\t\tpermette di tornare al Terminale Generale \n" + 
                    "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
                    "info\t\tpermette di visualizzare le informazioni di questo client\n" +
                    "set\t\tpermette di modificare la socket oppure il nome del server\n\t\t(set name nuovoNome) permette di cambiare il nome del server\n\t\t(set port nuovaPorta) permette di cambiare la porta del server\n"+
                    "$\t\tpermette di inviare un comando al server, invia $help per sapere tutta la lista di comandi disponibili\n"+
                    "file\t\tpermette di selezionare o creare un file nel quale stampare le risposte del server\n\t\t(file filepath )"
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
                case "port":
                case "p":
                    if(params.length == 3)return new CommandSetSocketServer(this.getGestore(),params[2]);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }
            case "en":
            case "enable": 
                return new CommandAttivaServer(this.getGestore());
            case "dis":
            case "disable": 
                return new CommandDisattivaServer(this.getGestore());
            
            default:    
                return new CommandDefault(params);
        }
    }
    
}
