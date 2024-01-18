package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.GestoreClientServer;

/**
    CommandFactoryGestore.
    Factory per la creazione di specifici comandi per il GestoreClientServer
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/ 
public class CommandFactoryGestore extends CommandFactoryI<GestoreClientServer> implements CommandFactory{

    /**
        Costruttore di default di CommandFactoryGestore.
        @param gestore è l'oggetto che farà da receiver per i comandi
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandFactoryGestore(GestoreClientServer gestore) throws CommandException{
        super(gestore);
    }

    /**
        getCommand.
        Metodo che, in base ai parametri, ritorna il comando corrispondente.
        @param params array di string contenente i parametri da cui instanziare i comandi corretti
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public Command getCommand(String []params) throws CommandException {

        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp(            
                "Comandi Terminale Generale\n\n"+
                "help\t\tpermette di visualizzare tutti i comandi \n" + 
                "quit\t\tpermette di terminare l'esecuzione \n" + 
                "show\t\tpermette di visualizzare la lista di tutti i client e server creati\n\t\t(show all) per visualizzare lista di client e server,\n\t\t(show client) per visualizzare solo la lista di client,\n\t\t(show server) per visualizzare solo la lista di server\n"+
                "info\t\tpermette di visualizzare le informazioni di uno specifico client o server (info nome)\n" +
                "new \t\tpermette di creare un server o client specifico\n\t\t(new client nomeClient ip porta) l'ip e la porta si riferiscono al socket remoto destinatario\n\t\t(new server nomeServer porta) la porta si riferisce alla porta su cui creare la nuova Socket locale\n\t\tse si specifica solo il nome(sia in client che server) sarà necessario attivarlo in seguito\n" + 
                "select\t\tpermette di selezionare un server o client in base al nome\n\t\t(select client nomeClient) permette di selezionare un client\n\t\t(select server nomeServer) permette di selezionare un server\n"+
                "delete\t\tpermette di eliminare un server o client in base al nome\n\t\t(delete client nomeClient) permette di eliminare un client\n\t\t(delete server nomeServer) permette di eliminare un server\n" +
                "undo\t\tpermette di annullare l'ultima operazione significativa eseguita (new e delete)\n"
                );
            case "sh":
            case "show":
            
                switch (params == null || params.length <= 1 ? "" : params[1]) {
                case "a":
                case "all":
                    if(params.length == 2)return new CommandShowAll(this.getGestore());
                case "c":
                case "client":
                    if(params.length == 2)return new CommandShowClient(this.getGestore());
                case "s":
                case "server":
                    if(params.length == 2)return new CommandShowServer(this.getGestore());
                default:
                    throw new CommandException("Errore, non è stato specificato cosa stampare");
                }
            case "i":
            case "info":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "c":
                case "client":
                    if(params.length == 3) return new CommandInfo(this.getGestore(),true,params[2]);
                case "s":
                case "server":
                    if(params.length == 3) return new CommandInfo(this.getGestore(),false,params[2]);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa stampare");
                }
            case "new":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "c":
                case "client":
                    if(params.length == 3)return new CommandNewClient(this.getGestore(),this.getGestore().getTerminalClient(),params[2]);
                    if(params.length == 5)return new CommandNewClient(this.getGestore(),this.getGestore().getTerminalClient(),params[2],params[3],params[4]);
                case "s":
                case "server":
                    if(params.length == 3)return new CommandNewServer(this.getGestore(),this.getGestore().getTerminalServer(),params[2]);
                    if(params.length == 4)return new CommandNewServer(this.getGestore(),this.getGestore().getTerminalServer(),params[2],params[3]);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa creare");
                }
            case "del":
            case "delete":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "c":
                case "client":
                    if(params.length == 3)return new CommandDeleteClient(this.getGestore(),params[2]);
                case "s":
                case "server":
                    if(params.length == 3)return new CommandDeleteServer(this.getGestore(),params[2]);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa eliminare");
                }
            case "sel":
            case "select":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "c":
                case "client":                      
                    if(params.length == 3)return new CommandSelectClient(this.getGestore(),params[2]);
                case "s":
                case "server":
                    if(params.length == 3)return new CommandSelectServer(this.getGestore(),params[2]);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }    
            default:    
                return new CommandDefault(params);
        }

    }

}
