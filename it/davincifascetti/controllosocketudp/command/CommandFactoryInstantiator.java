package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;

/**Permette di instanziare un istanza di CommandFactory adeguata
 * 
 */
public abstract class CommandFactoryInstantiator {
    
    public static CommandFactory newInstance(Commandable gestore) throws CommandException{

        if(gestore.getClass().equals(GestoreClientServer.class))return (CommandFactory) new CommandFactoryGestore((GestoreClientServer)gestore);
        else if(gestore.getClass().equals(Client.class))return (CommandFactory) new CommandFactoryClient((Client)gestore); 
        else if(gestore.getClass().equals(Server.class))return (CommandFactory) new CommandFactoryServer((Server)gestore);
        else throw new CommandException("non è stata inserita una classe");
    }
}
