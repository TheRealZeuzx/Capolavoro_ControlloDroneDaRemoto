package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Ui;

/**
 * TODO FIX server/client
 * Questo comando permette di ricercare tramite il gestoreClientServer un client o server specifico e di mostrarne il toString
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandInfo extends CommandI<GestoreClientServer>{
    private String nome;  
    private boolean client = false;  
    private boolean server = false;  
    /**
        Costruttore di CommandInfo.
     * @param gestore GestoreClientServer su cui effettuare la riceraca
     * @param params nome del client o server di cui stampare le info
     * @throws CommandException
     */
    public CommandInfo(GestoreClientServer gestore,String params,Ui ui) throws CommandException{
        super(gestore,params,ui);
        String[] temp = params.split("[ ]+");
        if(temp.length == 2)
            this.nome = temp[1];
        else 
            throw new CommandException("Errore, '" + this.getParams() +"' non è un parametro corretto, prova -> (client | server) (nome)");

        if(CommandFactoryCli.controllaRegexAssoluta("^s(?:e(?:r(?:v(?:e(?:r)?)?)?)?)?[ ]*$",temp[0]))
            this.server = true;
        else if(CommandFactoryCli.controllaRegexAssoluta("^c(?:l(?:i(?:e(?:n(?:t)?)?)?)?)?[ ]*$",temp[0]))
            this.client = true;
        else{
            throw new CommandException("Errore, '" + this.getParams() +"' non è un parametro corretto, prova -> (server | client) (nome)");
        }
    }
    public void execute() throws CommandException{
        if(client && server) throw new CommandException("Errore, non è stato specificato che gestore stampare!");
        if(client){
            if(this.getGestore().isEmpty(true)) throw new CommandException("La lista di client è vuota");
            Client temp = this.getGestore().ricercaClient(nome);
            if(temp == null)throw new CommandException("il client ricercato non esiste"); 
            System.out.println(temp.toString());
        }else if(server){
            if(this.getGestore().isEmpty(false)) throw new CommandException("La lista di server è vuota");
            Server temp = this.getGestore().ricercaServer(nome);
            if(temp == null)throw new CommandException("il server ricercato non esiste");
            System.out.println(temp.toString());
        }else  throw new CommandException("Errore, non è stato specificato che gestore stampare!");
        
        
    }
    
}
