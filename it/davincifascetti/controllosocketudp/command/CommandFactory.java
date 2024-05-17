package it.davincifascetti.controllosocketudp.command;

/**
 * FACTORY
    Interface CommandFactory
    Interfaccia per la generalizzazione delle factory.
    @author Tommaso Mussaldi
    @version 1.0
*/
public interface CommandFactory {
    /**
        getCommand.
        Metodo che, in base ai parametri, ritorna il comando corrispondente.
        @param params stringa contenente i parametri da cui instanziare i comandi corretti
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public abstract Command getCommand(String params)  throws CommandException;

}
