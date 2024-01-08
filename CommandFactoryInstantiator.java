public abstract class CommandFactoryInstantiator {

    public static CommandFactory newInstance(Class<?> tipoClasse) throws CommandException{
        if(tipoClasse.equals(GestoreClientServer.class))return new CommandFactoryGestore();
        else if(tipoClasse.equals(Client.class))return new CommandFactoryClient(); 
        else if(tipoClasse.equals(Server.class))return new CommandFactoryServer();
        else return null;
    }
}
