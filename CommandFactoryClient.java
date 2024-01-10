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
                return new CommandHelp("inserisci help client (CommandFactoryClient)");
            case "change":
                //TODO change name | change remote ip port (se inserisco current allora rimane invariato uno dei due campi)
            case "start":
            case "interrupt":
            case "end":
            default:    
                return new CommandDefault(params);
        }
    }
    
}
