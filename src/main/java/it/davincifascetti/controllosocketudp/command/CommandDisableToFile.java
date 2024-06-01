
package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Ui;
/**
 * SERVER
    Command Disable To File Server.
    Si occupa di gestire la disattivazione della stampa dei msg su file
    @author Mussaldi Tommaso, Bonfiglio mattia
    @version 1.0
*/
public class CommandDisableToFile extends CommandI<Server> implements UndoableCommand{
    private boolean append;
    private String nomeFile;
    /**
        Costruttore di default di disattiva ToFile.
        Richiama al costruttore del padre (CommandI).
        @param gestore Gestore su cui effettuare le operazioni. 
    */
    public CommandDisableToFile(Server gestore,String params,Ui ui) throws CommandException {
        super(gestore,"",ui);
        this.nomeFile = this.getGestore().getFileLogger().getFileName();
        this.append = this.getGestore().getFileLogger().isAppend();
    }

    /**
        Attiva la funzione "disableSuFile" del gestore corrispondente.
     */
    public void execute(){
        this.getGestore().disableSuFile();  
    }

    @Override
    public boolean undo() throws CommandException{
        new CommandEnableToFile(this.getGestore(),this.nomeFile + " " + append).execute();
        return true;
    }
    

}
