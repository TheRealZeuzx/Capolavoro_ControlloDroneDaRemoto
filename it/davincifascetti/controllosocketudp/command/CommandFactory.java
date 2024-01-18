package it.davincifascetti.controllosocketudp.command;


/**
    Interface CommandFactory
    Interfaccia per la generalizzazione delle factory.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public interface CommandFactory {
    /**
        getCommand.
        Metodo che, in base ai parametri, ritorna il comando corrispondente.
        Nell' implementazione abbiamo usato switch case ma sarebbe stato meglio creare un Dictionary che prendesse il command e la chiave in modo da poter 
        ampliare facilmente i comandi disponibili con un apposita registrazione, e renderlo possibile anche ad un utente , magari con la creazione di
        un apposita classe che implementi il comando per client e server.
        @param params array di string contenente i parametri da cui instanziare i comandi corretti
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public abstract Command getCommand(String[] params)  throws CommandException;
}
