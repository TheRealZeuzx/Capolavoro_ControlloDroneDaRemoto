package it.davincifascetti.controllosocketudp.command;

/**CommandHelp permette di stampare il msg di help richiesto
 * 
 */
public class CommandHelp implements Command{
    private String msg;
    public CommandHelp(String msg){
        this.msg = msg;
    }
    public void execute() {
        System.out.println(this.msg);
    }
}
