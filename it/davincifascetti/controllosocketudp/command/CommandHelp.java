package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.ServerThread;

/** 
    CommandHelp. 
    Permette di stampare il msg di help richiesto.
    @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandHelp extends CommandI<ServerThread>{
    private String msg;
    private ServerThread gestore = null;



    /** 
        Costruttore di CommandHelp, permette di invaire il messaggio di help o un messaggio in generale
        @param msg Messaggio da stampare
        @param gestore se è null, non viene inviato al client il messaggio altrimenti se passo ServerThread viene invaito al client il msg
     * @throws CommandException 
    */
    public CommandHelp(ServerThread gestore,String msg) throws CommandException{
        super(gestore, msg);
    }
    public void execute() throws CommandException {
        if(this.gestore == null)
            System.out.println(this.msg);
        else{
            try {
                this.gestore.inviaMsg(this.msg);
            } catch (CommandableException e) {
                throw new CommandException(e.getMessage());
            }
        }
    }
}
