package it.davincifascetti.controllosocketudp.program;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;



public class GestoreRisposte extends Component implements EventListenerRicezioneBuffer,EventListenerCommandable {
    private Ui riferimentoUi;
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

    private void gestisciRisposta(byte[] buffer, int length, ServerThread s) throws CommandException{
        String msgRicevuto = this.getMsgRicevuto(buffer, length);
        System.out.println("la risposta a: '" + msgRicevuto + "' verrà ora gestita");
        if(this.factory != null){
            try{
                this.executeCommand(this.factory.getCommand(s,msgRicevuto,this.riferimentoUi),s);
            }catch(CommandException e){
                this.add(s.getServer()).stampaVideo(e.getMessage(),this.getUi(),s.getServer()); 
            }catch(ErrorLogException e){
                this.add(s.getServer()).errorLog(e.getMessage(), true,this.getUi(),s.getServer());
            }
        }
    }

    public void defaultResponse(String message,Server s) throws CommandableException{
        InfoServer temp = this.add(s);
        if(temp == null) throw new CommandableException("Errore, qualcosa è andato storto!");
        if(temp.getFileLogger() != null)
            temp.fileLog(message,s);
        temp.stampaVideo("il client dice: " + message,this.getUi(),s);
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
        if(pos)
            return null;
        return this.mapInfoServer.get(s);
    }

    public void remove(Server s){
        if(s == null) return;
        this.mapInfoServer.remove(s);
    }

    

    @Override
    public void setUi(Ui ui) throws CommandException {
        if(ui == null) throw new CommandException("Errore, la UI passata è null!");
        this.riferimentoUi = ui;
    }

    



    @Override
    public void update(byte[] buffer, int lung,Commandable commandable) {
        if(ServerThread.class.isInstance(commandable)){
            try {
                this.gestisciRisposta(buffer, lung, (ServerThread)commandable);
            } catch (CommandException e) {
                //credo notify 
            }
        }
    }


    @Override
    public void update(String eventType, Commandable commandable) {
        if(eventType.equals(GestoreClientServer.SERVER_RIMOSSO)){
            if(Server.class.isInstance(commandable))
                this.remove((Server)commandable);
        }
    }


    
    @Override
    public void setManager (CommandListManager manager) throws CommandException{
        super.setManager(manager);
        this.factory.setManager(manager);
    }


}
