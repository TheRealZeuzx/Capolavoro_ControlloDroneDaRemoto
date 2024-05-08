package it.davincifascetti.controllosocketudp.command;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**permette di instanziare un nuovo client e inserirlo nella lista di client
     * @param gestore GestoreClientServer che farà da reciever e quindi instanziera e inserira il client
     * @param terminale terminale da di client da passare al costruttore di client
     * @param nomeIpPorta stringa contenente nome, ip e porta del client, separati da uno spazio, se 
     * @throws CommandableException
     */
    public CommandNewClient(GestoreClientServer gestore, String nomeIpPorta) throws CommandException {
        super(gestore,nomeIpPorta);
        this.terminale = this.getGestore().getTerminalClient();


        //TODO farla funzionare e poi portarla in una classe apposita
        Pattern pattern = Pattern.compile("(\\b[^\\s]+\\b)");
        Matcher matcher = pattern.matcher(nomeIpPorta);
        String [] temp  = matcher.results().toArray(String[]::new);
        // String [] temp = nomeIpPorta != null && !nomeIpPorta.isBlank() && matcher.find() ? nomeIpPorta.split() : new String[0];
        // Remove empty strings from the array
        System.out.println(nomeIpPorta); //new client c1 localhost 1212
        System.out.println(temp[0]); //new client c1 localhost 1212
        System.out.println(temp[1]); //new client c1 localhost 1212
        System.out.println(temp[2]); //new client c1 localhost 1212
        if(temp.length == 0) 
        if(temp.length == 1)
        this.nome = temp[0];
        else if(temp.length == 3){
            this.nome = temp[0];
            this.ip = temp[1];
            this.porta = temp[2];
        }else
        throw new CommandException("I parametri passati non sono validi!");
        
        
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
        new CommandDeleteClient(this.getGestore(), nome).execute();
        return true;
    }
    
}
