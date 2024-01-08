public abstract class CommandFactoryInstantiator {

    public static CommandFactory newInstance(Class<?> tipoClasse) throws CommandException{
        if(tipoClasse.equals(GestoreClientServer.class))return ((CommandFactory<GestoreClientServer>) new CommandFactoryGestore());
        else if(tipoClasse.equals(Client.class))return ((CommandFactory<Client>)new CommandFactoryClient()); 
        else if(tipoClasse.equals(Server.class))return ((CommandFactory<Server>) new CommandFactoryServer());
        else return null;
    }
}
