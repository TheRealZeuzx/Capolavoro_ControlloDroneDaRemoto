package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.ServerThread;

/**Factory per la creazione di specifici comandi per il Server

 * 
 */
public class CommandFactoryRisposta implements CommandFactory{
    private ServerThread gestore;
    public CommandFactoryRisposta(ServerThread gestore) throws CommandException{
        this.gestore = gestore;
    }
    public Command getCommand(String []params) throws CommandException {
        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch(scelta){
            // l- ciao mi chiamo marco
            case "l":
            case "log":
                
            break;
            case "f":
            case "file": 
                

            break;
            case "r":
            case "remote": 

            break;
            case "h":
            case "help":
                if(params.length == 1){
                    CommandHelp temp = new CommandHelp("HELP",this.gestore);
                }
            break;
            default: 
                //TODO se è sbagliato il comando allora invia msg comando errato
            break;
        }
        
    }

}
