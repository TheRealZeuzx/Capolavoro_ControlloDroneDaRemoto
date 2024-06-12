package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Ui;
/** permette di instanziare un nuovo client all'interno della lista client di GestoreClientServer
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
    @version 1.0
 */
public class CommandNewClient extends CommandI<GestoreClientServer> implements UndoableCommand{

    private String nome;
    private String porta = null;
    private String ip = "";
    private String desc = null;

    /**permette di instanziare un nuovo client e inserirlo nella lista di client
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il client
     * @param terminale terminale da di client da passare al costruttore di client
     * @param nomeIpPorta stringa contenente nome, ip e porta del client, separati da uno spazio, se 
     * @throws CommandableException
     */
    public CommandNewClient(GestoreClientServer gestore, String nomeIpPorta,Ui ui) throws CommandException {
        super(gestore,nomeIpPorta,ui);
        String []temp =  nomeIpPorta.split("[ ]+");
        if(temp.length == 1)
            this.nome = temp[0]; //c1 localhost 1212
        else if(temp.length == 3){
            this.nome = temp[0];
            this.ip = temp[1];
            this.porta = temp[2];
        }else
            throw new CommandException("I parametri passati non sono validi!");
    }
    public CommandNewClient(GestoreClientServer gestore, String nomeIpPorta,Ui ui,String desc) throws CommandException {
        this(gestore, nomeIpPorta, ui);
        this.desc = desc;
    }

    @Override
    public void execute() throws CommandException,ErrorLogException {
        try{
        if(ip.equals("") || porta == null)
            this.getGestore().newClient(nome,desc);
        else
            this.getGestore().newClient(nome,ip,porta,desc);
        }catch(CommandableException e){
            throw new CommandException(e.getMessage());
        }
    }

    /**permette di eliminare il client inserito in precedenza
     * 
     */
    @Override
    public boolean undo() throws CommandException {
        new CommandDeleteClient(this.getGestore(), nome,this.getUi()).execute();
        return true;
    }
    
}
