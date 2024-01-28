

package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Server;
/**
    Command Enable To File Server.
    Si occupa di gestire la disattivazione della stampa dei msg su file
    @author Mussaldi Tommaso, Bonfiglio mattia
    @version 1.0
*/
public class CommandEnableToFile extends CommandI<Server> implements UndoableCommand{


    private String nomeFile;
    private boolean append;
    /**
        Costruttore di default di disattiva ToFile.
        Richiama al costruttore del padre (CommandI).
        @param gestore Gestore su cui effettuare le operazioni. 
        @param nomeFile nome del file su cui stampare
        @param append true se si vuole la modalita append altrimenti false
    */
    public CommandEnableToFile(Server gestore, String nomeFile,boolean append) throws CommandException {
        super(gestore);
        this.append = append;
        this.nomeFile = nomeFile;
    }

    /**
        Attiva la funzione "SuFile" del gestore corrispondente.
     * @throws CommandException
     */
    public void execute() throws CommandException{
        try {
            this.getGestore().SuFile(append, nomeFile);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean undo() throws CommandException{
        new CommandDisableToFile(this.getGestore()).execute();
        return true;
    }
    

}
