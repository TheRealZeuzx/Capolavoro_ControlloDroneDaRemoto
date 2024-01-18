package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Terminal;
/** permette di instanziare un nuovo client all'interno della lista client di GestoreClientServer
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandNewClient extends CommandI<GestoreClientServer> implements UndoableCommand{

    private String nome;
    private Terminal<Client> terminale;
    private String porta = null;
    private String ip = "";
    /**permette di instanziare un nuovo client e inserirlo nella lista di client, utilizza il primo costruttore di client
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il client
     * @param terminale terminale da di client da passare al costruttore di client
     * @param nome nome del client
     * @throws CommandableException
     */
    public CommandNewClient(GestoreClientServer gestore,Terminal<Client> terminale, String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
        this.terminale = terminale;
    }
    /**permette di instanziare un nuovo client e inserirlo nella lista di client, utilizza il secondo costruttore di client
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il client
     * @param terminale terminale da di client da passare al costruttore di client
     * @param nome nome del client
     * @param ip ip del server remoto
     * @param porta porta del server remoto
     * @throws CommandableException
     * @throws ErrorLogException
     */
    public CommandNewClient(GestoreClientServer gestore,Terminal<Client> terminale, String nome,String ip,String porta) throws CommandException {
        this(gestore, terminale, nome);
        this.ip = ip;
        this.porta = porta;
    }

    @Override
    public void execute() throws CommandException,ErrorLogException {
        try{
        if(ip.equals("") || porta == null)
            this.getGestore().newClient(terminale,nome);
        else
            this.getGestore().newClient(terminale,nome,ip,porta);
        }catch(CommandableException e){
            throw new CommandException(e.getMessage());
        }
    }

    /**permette di eliminare il client inserito in precedenza
     * 
     */
    @Override
    public boolean undo() throws CommandException {
        new CommandDeleteClient(getGestore(), nome).execute();
        return true;
    }
    
}
