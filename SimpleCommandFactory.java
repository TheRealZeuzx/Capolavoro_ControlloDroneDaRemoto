public class SimpleCommandFactory {
    
    public SimpleCommandFactory(){}
    public Command getCommand(GestoreClientServer gestore, String []params) throws CommandException {

        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp();
            case "s":
            case "show":
                return new CommandShow(gestore,params);
            case "i":
            case "info":
                return new CommandInfo(gestore,params);
            default:    
                return new CommandDefault(params);
        }
    }

}
