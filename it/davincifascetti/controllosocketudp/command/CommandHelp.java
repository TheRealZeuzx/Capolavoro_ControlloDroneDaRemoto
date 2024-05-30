package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Terminal;
import it.davincifascetti.controllosocketudp.program.Ui;

/** 
 * GENERALE
    CommandHelp. 
    Permette di stampare il msg di help richiesto.
    @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandHelp<T extends Commandable> extends CommandI<T>{


    /** 
        Costruttore di CommandHelp, permette di invaire il messaggio di help
        @param msg Messaggio da stampare
        @param gestore se è null, non viene inviato al client il messaggio altrimenti se passo ServerThread viene invaito al client il msg
     * @throws CommandException 
    */
    public CommandHelp(T gestore,String msg,Ui ui) throws CommandException{
        super(gestore, msg,ui);
        if(!Terminal.class.isInstance(ui)) throw new CommandException("Errore, la Ui passata non è un Terminal!");

    }
    public void execute() throws CommandException {

        System.out.println(getUi().getUser().getManager().getCommandList(this.getGestore().getClass()).getStringaHelp());
    }
}
