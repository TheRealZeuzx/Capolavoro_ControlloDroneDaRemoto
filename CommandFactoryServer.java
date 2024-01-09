/**Factory per la creazione di specifici comandi per il Server

 * 
 */
public class CommandFactoryServer extends CommandFactoryI<Server>{
    public CommandFactoryServer(Server gestore) throws CommandException{
        super(gestore);
    }
    public Command getCommand(String []params) throws CommandException {

        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp( "help server");
            case "s":
            default:    
                return new CommandDefault(params);
        }
    }

}
