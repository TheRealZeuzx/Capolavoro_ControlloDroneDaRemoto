public class CommandFactoryClient implements  CommandFactory{


    public CommandFactoryClient(){}
    @Override
    public Command getCommand(Client gestore, String[] params) throws CommandException {
        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp("inserisci help client (CommandFactoryClient)");
            default:    
                return new CommandDefault(params);
        }
    }
    @Override
    public Command getCommand(GestoreClientServer gestore, String[] params) throws CommandException {
        return null;
    }
    @Override
    public Command getCommand(Server gestore, String[] params) throws CommandException {
         return null;
    }
    
}
