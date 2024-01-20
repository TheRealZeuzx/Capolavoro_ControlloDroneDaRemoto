package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.GestoreClientServer;

/**permette di stampare la lista di client presenti nel GestoreClientServer
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandShowClient extends CommandI<GestoreClientServer>{
    /**
     * 
     * @param gestore oggetto di cui fare la show di all
     * @throws CommandException
     */
    public CommandShowClient(GestoreClientServer gestore) throws CommandException{
        super(gestore);
    }
    public void execute() throws CommandException{
        if(!this.getGestore().stampaLista(true,false)) throw new CommandException("Errore nella stampa");
    }

}
