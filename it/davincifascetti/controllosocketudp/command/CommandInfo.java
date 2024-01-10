package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;

/**Questo comando permette di ricercare tramite il gestoreClientServer un client o server specifico e di mostrarne il toString
 * 
 */
public class CommandInfo extends CommandI<GestoreClientServer>{
    private String nome;  
    private boolean client;  
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
