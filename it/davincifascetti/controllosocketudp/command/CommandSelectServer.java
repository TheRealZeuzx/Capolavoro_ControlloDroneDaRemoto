package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;
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
    public CommandSelectServer(GestoreClientServer gestore,String nome) throws CommandException {
        super(gestore,nome);
    }
    public void execute() throws CommandException {
        Server temp = this.getGestore().ricercaServer(this.getParams());
        if(temp == null) throw new CommandException("il server '" + this.getParams() + "' non esiste");
        temp.startTerminal();
    }
    

}
