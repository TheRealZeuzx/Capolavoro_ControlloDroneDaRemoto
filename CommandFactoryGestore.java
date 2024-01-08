public class CommandFactoryGestore implements CommandFactory{
    
    public CommandFactoryGestore(){}
    public Command getCommand(GestoreClientServer gestore, String []params) throws CommandException {

        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp(            
                "-------------------------------------------------------------------------------\n"+
                "Comandi Terminale Generale\n\n"+
                "help\t\tpermette di visualizzare tutti i comandi \n" + 
                "quit\t\tpermette di terminare l'esecuzione \n" + 
                "show\t\tpermette di visualizzare la lista di tutti i client e server creati\n\t\t(show all) per visualizzare lista di client e server,\n\t\t(show client) per visualizzare solo la lista di client,\n\t\t(show server) per visualizzare solo la lista di server\n"+
                "info\t\tpermette di visualizzare le informazioni di uno specifico client o server (info nome)\n" +
                "new \t\tpermette di creare un server o client specifico\n\t\t(new client nomeClient ip porta) l'ip e la porta si riferiscono al socket remoto destinatario\n\t\t(new server nomeServer porta) la porta si riferisce alla porta su cui creare la nuova Socket locale\n" +
                "select\t\tpermette di selezionare un server o client in base al nome\n\t\t(select client nomeClient) permette di selezionare un client\n\t\t(select server nomeServer) permette di selezionare un server\n"+
                "delete\t\tpermette di eliminare un server o client in base al nome\n\t\t(delete client nomeClient) permette di eliminare un client\n\t\t(delete server nomeServer) permette di eliminare un server\n" +
                "undo\t\tpermette di annullare l'ultima operazione significativa eseguita (new e delete)\n"
                );
            case "s":
            case "show":
            
                switch (params == null || params.length == 1 ? "" : params[1]) {
                case "a":
                case "all":
                    return new CommandShowAll(gestore);
                case "c":
                case "client":
                    return new CommandShowClient(gestore);
                case "s":
                case "server":
                    return new CommandShowServer(gestore);
                default:
                    throw new CommandException("Errore, non è stato specificato cosa stampare");
                }
            case "i":
            case "info":
                return new CommandInfo(gestore,params);
            default:    
                return new CommandDefault(params);
        }

    }
    @Override
    public Command getCommand(Client gestore, String[] params) throws CommandException {
        return null;
    }
    @Override
    public Command getCommand(Server gestore, String[] params) throws CommandException {
         return null;
    }

}
