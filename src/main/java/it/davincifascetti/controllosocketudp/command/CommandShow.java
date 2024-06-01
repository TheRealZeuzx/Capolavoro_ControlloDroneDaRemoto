package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Ui;

/**permette di stampare la lista di client presenti nel GestoreClientServer
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandShow extends CommandI<GestoreClientServer>{
    private boolean client = false;
    private boolean server = false;

    /**
     * 
     * @param gestore oggetto di cui fare la show di all
     * @param params può essere ignorato
     * @throws CommandException
     */
    public CommandShow(GestoreClientServer gestore,String params,Ui ui) throws CommandException{
        super(gestore,params,ui);
        if(CommandFactoryI.controllaRegexAssoluta("^s(?:e(?:r(?:v(?:e(?:r)?)?)?)?)?[ ]*$",this.getParams()))
            this.server = true;
        else if(CommandFactoryI.controllaRegexAssoluta("^c(?:l(?:i(?:e(?:n(?:t)?)?)?)?)?[ ]*$",this.getParams()))
            this.client = true;
        else if(CommandFactoryI.controllaRegexAssoluta("^a(?:l(?:l)?)?[ ]*$",this.getParams())){
            this.client = true;this.server = true;
        }else
            throw new CommandException("Errore, '" + this.getParams() +"' non è un parametro corretto, prova -> (server | client | all)");
    }
    public void execute() throws CommandException{
        if(!this.getGestore().stampaLista(client,server) || (!client && !server)) throw new CommandException("Errore nella stampa");
    }

}
