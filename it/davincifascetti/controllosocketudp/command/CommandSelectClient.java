package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
/**permette di selezionare un client nella lista di client del gestoreclientserver (aprire il terminale con il client)
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandSelectClient extends CommandI<GestoreClientServer>{
    private String nome;
    /**
     * 
     * @param gestore gestoreclientserver che si occupera di ricercare il client
     * @param nome nome del client da selezionare
     * @throws CommandException
     */
    public CommandSelectClient(GestoreClientServer gestore,String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
    }
    public void execute() throws CommandException {
        Client temp = this.getGestore().ricercaClient(this.nome);
        if(temp == null) throw new CommandException("il client '" + this.nome + "' non esiste");
        temp.startTerminal();
    }
    

}

