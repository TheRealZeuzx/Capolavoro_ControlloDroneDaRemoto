package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Terminal;
import it.davincifascetti.controllosocketudp.program.Ui;
/**permette di selezionare un client nella lista di client del gestoreclientserver (aprire il terminale con il client)
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandSelectClient extends CommandI<GestoreClientServer>{
    /**
     * 
     * @param gestore gestoreclientserver che si occupera di ricercare il client
     * @param nome nome del client da selezionare
     * @throws CommandException
     */
    public CommandSelectClient(GestoreClientServer gestore,String nome,Ui ui) throws CommandException {
        super(gestore,nome,ui);
        if(!Terminal.class.isInstance(ui)) throw new CommandException("Errore, la Ui passata non è un Terminal!");
    }
    public void execute() throws CommandException {
        Client temp = this.getGestore().ricercaClient(this.getParams());
        if(temp == null) throw new CommandException("il client '" + this.getParams() + "' non esiste");
        
        ((Terminal)this.getUi()).getCli().setVista(temp);
    }
    

}

