package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;
import it.davincifascetti.controllosocketudp.program.Ui;

/** permette di instanziare un nuovo server all'interno della lista client di GestoreClientServer
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandNewServer extends CommandI<GestoreClientServer> implements UndoableCommand{

    private String nome;
    private String porta = null;
    private String ip = null;
    private String descrizione = null;
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il primo costruttore di server
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il server
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @throws CommandableException
     */
    public CommandNewServer(GestoreClientServer gestore,String nomePortaIp,Ui ui) throws CommandException {
        super(gestore, nomePortaIp,ui);
        String []temp =  nomePortaIp.split("[ ]+");
        if(temp.length == 1)
            this.nome = temp[0]; //new c c1 localhost 1212
        else if(temp.length == 3){
            this.nome = temp[0];
            this.ip = temp[1];
            this.porta = temp[2];
        }else
            throw new CommandException("I parametri passati non sono validi!");
    }

    public CommandNewServer(GestoreClientServer gestore,String nomePortaIp,Ui ui, String descrizione) throws CommandException {
        this(gestore, nomePortaIp,ui);
        this.descrizione = descrizione;
    }


    /**utilizza il metodo newServer 
     * 
     */
    @Override
    public void execute() throws CommandException, ErrorLogException {
        Server s = null;
        try{
            if(porta == null && ip == null)
                s = this.getGestore().newServer(nome, descrizione);
            else if(porta != null && ip != null) s = this.getGestore().newServer(nome,ip,porta,descrizione);
            else throw new CommandException("Errore, qualcosa è andato storto!");
        }catch(CommandableException e){
            throw new CommandException(e.getMessage());
        }
    }

    /**permette di eliminare il server appena creatp
     * 
     */
    @Override
    public boolean undo() throws CommandException {
        new CommandDeleteServer(getGestore(), nome,this.getUi()).execute();
       return true;
    }
    
}
