package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;

/**Questo comando permette di ricercare tramite il gestoreClientServer un client o server specifico e di mostrarne il toString
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandInfo extends CommandI<GestoreClientServer>{
    private String nome;  
    private boolean client;  
    /**
        Costruttore di CommandInfo.
     * @param gestore GestoreClientServer su cui effettuare la riceraca
     * @param client true se si vuole informazioni di un client false se si vogliono info di un server
     * @param nome nome del client o server di cui stampare le info
     * @throws CommandException
     */
    public CommandInfo(GestoreClientServer gestore,boolean client,String nome ) throws CommandException{
        super(gestore);
        this.nome=nome;
        this.client = client;
    }
    public void execute() throws CommandException{

        if(client){
            if(this.getGestore().isEmpty(true)) throw new CommandException("La lista di client è vuota");
            Client temp = this.getGestore().ricercaClient(nome);
            if(temp == null)throw new CommandException("il client ricercato non esiste"); 
            System.out.println(temp.toString());
        }else{
            if(this.getGestore().isEmpty(false)) throw new CommandException("La lista di server è vuota");
            Server temp = this.getGestore().ricercaServer(nome);
            if(temp == null)throw new CommandException("il server ricercato non esiste");
            System.out.println(temp.toString());
        }
        
        
    }
    
}
