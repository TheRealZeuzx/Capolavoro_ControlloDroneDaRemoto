package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;

/**
    CommandFactoryGestore.
    permette di instanziare una Factory adeguata in base al gestore (che implementa Commandable) inserito
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/ 
public abstract class CommandFactoryInstantiator {
    
    /**gestore di tipo commandable da cui viene instanziato la factory corrispondente
     * 
     * @param gestore è l'oggetto che farà da receiver per i comandi, dalla classe di questo oggetto si deciderà cosa instanzaire
     * @return restituisce CommandFactory (interfaccia comune a le factory che estendono CommandFactoryI<T extends Commandable>)
     * @throws CommandException
     */
    public static CommandFactory newInstance(Commandable gestore) throws CommandException{

        if(gestore.getClass().equals(GestoreClientServer.class))return (CommandFactory) new CommandFactoryGestore((GestoreClientServer)gestore);
        else if(gestore.getClass().equals(Client.class))return (CommandFactory) new CommandFactoryClient((Client)gestore); 
        else if(gestore.getClass().equals(Server.class))return (CommandFactory) new CommandFactoryServer((Server)gestore);
        else throw new CommandException("non è stata inserita una classe");
    }
}
