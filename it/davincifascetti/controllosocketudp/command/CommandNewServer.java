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
    public CommandNewServer(GestoreClientServer gestore,Terminal<Server> terminale, String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
        this.terminale = terminale;
    }
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il secondo costruttore di server 
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il server
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @param porta porta locale del server
     * @throws CommandableException
     * @throws ErrorLogException
     */
    public CommandNewServer(GestoreClientServer gestore,Terminal<Server> terminale, String nome,String porta) throws CommandException {
        this(gestore, terminale, nome);
        this.porta = porta;
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
