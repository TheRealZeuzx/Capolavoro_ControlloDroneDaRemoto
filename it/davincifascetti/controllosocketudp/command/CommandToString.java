package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.ServerThread;

/** 
 * GENERALE
    CommandHelp. 
    Permette di stampare il msg di help richiesto.
    @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandToString<T extends Commandable> extends CommandI<T>{


    /** 
        Costruttore di CommandHelp, permette di invaire il messaggio di help o un messaggio in generale
        @param msg Messaggio da stampare
        @param gestore se è null, non viene inviato al client il messaggio altrimenti se passo ServerThread viene invaito al client il msg
     * @throws CommandException 
    */
    public CommandToString(T gestore,String msg) throws CommandException{
        super(gestore, "");
    }
    public void execute() throws CommandException {

        System.out.println(this.getGestore().toString());

    }
}
