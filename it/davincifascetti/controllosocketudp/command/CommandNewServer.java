package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Terminal;

/** permette di instanziare un nuovo server all'interno della lista client di GestoreClientServer
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandNewServer extends CommandI<GestoreClientServer> implements UndoableCommand{

    private String nome;
    private Terminal<Server> terminale;
    private String porta = null;
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il primo costruttore di server
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il server
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @throws CommandableException
     */
    public CommandNewServer(GestoreClientServer gestore,String nomePorta) throws CommandException {
        super(gestore, nomePorta);
        this.terminale = gestore.getTerminalServer();
        String []temp =  nomePorta.split("[ ]+");
        if(temp.length == 1)
            this.nome = temp[0]; //new c c1 localhost 1212
        else if(temp.length == 2){
            this.nome = temp[0];
            this.porta = temp[1];
        }else
            throw new CommandException("I parametri passati non sono validi!");
    }


    /**utilizza il metodo newServer 
     * 
     */
    @Override
    public void execute() throws CommandException, ErrorLogException {
        try{
            if(porta == null)
                this.getGestore().newServer(terminale,nome);
            else
                this.getGestore().newServer(terminale,nome,porta);
        }catch(CommandableException e){
            throw new CommandException(e.getMessage());
        }
    }

    /**permette di eliminare il server appena creatp
     * 
     */
    @Override
    public boolean undo() throws CommandException {
        new CommandDeleteServer(getGestore(), nome).execute();
       return true;
    }
    
}
