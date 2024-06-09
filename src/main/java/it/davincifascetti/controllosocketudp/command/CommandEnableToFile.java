

package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Terminal;
import it.davincifascetti.controllosocketudp.program.Ui;
/**
 * SERVER
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
    public CommandEnableToFile(Server gestore, String nomeFileAppend,Ui ui) throws CommandException {
        super(gestore,nomeFileAppend,ui);
        String[] temp = this.getParams().split("[ ]+");
        if(temp.length != 2)throw new CommandException("Errore, '" + this.getParams() +"' non è un parametro corretto, prova -> (nomeFile) (append | overwrite)");
        this.nomeFile = temp[0];
        if(CommandFactoryI.controllaRegexAssoluta("^a(?:p(?:p(?:e(?:n(?:d)?)?)?)?)?[ ]*$",temp[1]))
            this.append = true;
        else if(CommandFactoryI.controllaRegexAssoluta("^o(?:v(?:e(?:r(?:w(?:r(?:i(?:t(?:e)?)?)?)?)?)?)?)?[ ]*$",temp[1]))
            this.append = false;
        else{
            throw new CommandException("Errore, '" + this.getParams() +"' non è un parametro corretto, prova -> (nomeFile) (append | overwrite)");
        }
    }

    /**
        Attiva la funzione "SuFile" del gestore corrispondente.
     * @throws CommandException
     */
    public void execute() throws CommandException{
        
        
        try {
            ((Terminal)this.getUi()).getGestoreRisposte().add(this.getGestore()).setFileLogger(append, nomeFile, this.getGestore().getNome());;
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
