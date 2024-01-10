package it.davincifascetti.controllosocketudp.command;

/**CommandDefualt si occupa di stampare a video il msg di default dello switchcase (nel caso in cui il comando sia errato)
 * 
 */
public class CommandDefault implements Command{
    private String msg;
    public CommandDefault(String[] params) throws CommandException{
        this.msg = "";
        if(params != null && params.length != 0){
            for (String string : params) {
                this.msg += string;
            }
        }
    }
    public void execute() throws CommandException{
        System.out.println("il comando '"+ this.msg + "' non è riconosciuto, (digita 'help' per mostrare lista di comandi disponibili)");
    }
}
