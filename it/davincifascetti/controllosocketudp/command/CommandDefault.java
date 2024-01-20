package it.davincifascetti.controllosocketudp.command;

/**
    CommandDefault. 
    È ciò occupa di stampare a video il messaggio di default dello switchcase (nel caso in cui il comando sia errato) 
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/
public class CommandDefault implements Command{
    private String msg;
    /**
        Costruttore di default di CommandDefault.
        Questo comando viene eseguito in caso il comando inserito dall'utente non sia riconosciuto.
        @param params comando non riconosciuto
    */
    public CommandDefault(String[] params) throws CommandException{
        this.msg = "";
        if(params != null && params.length != 0){
            for (String string : params) {
                this.msg += string +" ";
            }
            this.msg = this.msg.substring(0,this.msg.length()-1);
        }
    }

    /**
        Stampa a video il messaggio di errore.
     */
    public void execute() throws CommandException{
        System.out.println("il comando '"+ this.msg + "' non è riconosciuto, (digita 'help' per mostrare lista di comandi disponibili)");
    }
}
