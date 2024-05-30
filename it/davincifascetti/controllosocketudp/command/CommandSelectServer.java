package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Terminal;
import it.davincifascetti.controllosocketudp.program.Ui;
/**permette di selezionare un server nella lista di server del gestoreclientserver (aprire il terminale con il server)
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandSelectServer extends CommandI<GestoreClientServer>{
    /**
     * 
     * @param gestore gestoreclientserver che si occupera di ricercare il server
     * @param nome nome del server da selezionare
     * @throws CommandException
     */
    public CommandSelectServer(GestoreClientServer gestore,String nome,Ui ui) throws CommandException {
        super(gestore,nome,ui);
        if(!Terminal.class.isInstance(ui)) throw new CommandException("Errore, la Ui passata non è un Terminal!");
    }
    public void execute() throws CommandException {
        Server temp = this.getGestore().ricercaServer(this.getParams());
        if(temp == null) throw new CommandException("il server '" + this.getParams() + "' non esiste");
        ((Terminal)this.getUi()).getCli().setVista(temp);
    }
    

}
