package it.davincifascetti.controllosocketudp.program;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;


/**specifico per la Ui Terminal
 * 
 */
//!volendo possiamo creare un gestore risposte client e uno server in modo da avere anche comandi separati per i due 
public class GestoreRisposte extends Component{
    private CommandFactoryI factory;
    private Map<Server,InfoServer> mapInfoServer = Collections.synchronizedMap(new HashMap<Server,InfoServer>());

    public GestoreRisposte(CommandListManager manager) throws CommandException {
        this.factory = new CommandFactoryI();
        this.setManager(manager);
    }
    public GestoreRisposte(CommandListManager manager,Ui ui) throws CommandException {
        this(manager);
        this.setUi(ui);
    }

    public void gestisciRisposta(byte[] buffer, int length, ServerThread s) throws CommandException,ErrorLogException{
        String msgRicevuto = this.getMsgRicevuto(buffer, length);
        ((Terminal)this.getUi()).getCli().print("il "+s.getClass().getSimpleName()+" gestirà il messaggio: '" + msgRicevuto + "' appena ricevuto");
        if(this.factory != null){
            this.executeCommand(this.factory.getCommand(s,msgRicevuto,this.getUi()),s);
            this.add(s);
        }
    }
    public void gestisciRisposta(byte[] buffer, int length, Client c) throws CommandException,ErrorLogException{
        String msgRicevuto = this.getMsgRicevuto(buffer, length);
        ((Terminal)this.getUi()).getCli().print("il server dice: \n" + msgRicevuto);

    }
    

    public void defaultResponse(String message,Server s) throws CommandableException{
        InfoServer temp = this.add(s);
        if(temp == null) throw new CommandableException("Errore, qualcosa è andato storto!");
        if(temp.getFileLogger() != null)
            temp.fileLog(message,s);
        temp.addMsg(message);
        if(((Terminal)this.getUi()).getCli().isAttivo(s)) 
            ((Terminal)this.getUi()).getCli().print("il client dice: " + message);
    }



    /**permette di estrapolare il messaggio ricevuto dal pacchetto ricevuto
     * 
     * @return stringa contenente il msg ricevuto dal client
     */
    private String getMsgRicevuto(byte[] buffer, int length) throws CommandException{
        if(buffer == null || length < 0) throw new CommandException("Errore, buffer o length errati");
		String msgRicevuto = new String(buffer);
		return msgRicevuto.substring(0, length);
        
    }

    public InfoServer add(Server s){
        InfoServer temp = this.findLista(s);
        if(temp == null){ 
            temp = new InfoServer();
            this.mapInfoServer.put(s, temp);
        }
        return temp;
    }

    private InfoServer findLista(Server s){
        boolean pos = this.mapInfoServer.containsKey(s);
        if(!pos)
            return null;
        return this.mapInfoServer.get(s);
    }

    public InfoServer getInfo(Server s){
        return this.findLista(s);
    }

       /**restitsce stringa contenente tutti i msg ricevuti dal client concatenati (aggiunge \n)
     * 
     * @return stringa contenente tutti i msg ricevuti dal client concatenati (aggiunge \n)
     */
    public String stampaStoriaMsg(Server s){
        InfoServer temp1 = getInfo(s);
        if(temp1 == null) return "";
        String temp = "";   
        for (String string : temp1.getStoriaMsg()) {
            temp +=string+"\n";
        }
        return temp.substring(0,temp.length() - 1);//tolgo l'ultimo '\n'
    }


    public void remove(Server s){
        if(s == null) return;
        this.mapInfoServer.remove(s);
    }

    // @Override
    // public void update(byte[] buffer, int lung,Commandable commandable) {
    //     if(ServerThread.class.isInstance(commandable)){
    //         try {
    //             this.gestisciRisposta(buffer, lung, (ServerThread)commandable);
    //         } catch (CommandException e) {
    //             //credo notify 
    //         }
    //     }
    // }


    // @Override
    // public void update(String eventType, Commandable commandable) {
    //     if(eventType.equals(GestoreClientServer.SERVER_RIMOSSO)){
    //         if(Server.class.isInstance(commandable))
    //             this.remove((Server)commandable);
    //     }
    // }


    
    @Override
    public void setManager (CommandListManager manager) throws CommandException{
        super.setManager(manager);
        this.factory.setManager(manager);
    }

    public void destroy(){
        super.destroy();
        this.mapInfoServer = null;
    }

}
