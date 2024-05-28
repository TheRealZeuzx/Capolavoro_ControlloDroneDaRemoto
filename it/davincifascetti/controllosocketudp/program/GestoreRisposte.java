package it.davincifascetti.controllosocketudp.program;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;



public class GestoreRisposte implements EventListenerRicezioneBuffer,EventListenerCommandable,Component {
    private Ui riferimentoUi;
    private CommandFactoryI factory;
    private CommandListManager manager;

    private Map<Server,InfoServer> mapInfoServer = Collections.synchronizedMap(new HashMap<Server,InfoServer>());

    public GestoreRisposte(CommandListManager manager,Ui ui) throws CommandException {
        this.setUi(ui);
        this.factory = new CommandFactoryI();
        this.setManager(manager);
    }

    private void gestisciRisposta(byte[] buffer, int length, ServerThread s) throws CommandException{
        String msgRicevuto = this.getMsgRicevuto(buffer, length);
        if(this.factory != null){
            try{
                this.riferimentoUi.executeCommand(this.factory.getCommand(s,msgRicevuto));
            }catch(CommandException e){
                // this.stampaVideo(e.getMessage()); //TODO Capire
            }catch(ErrorLogException e){
                // this.errorLog(e.getMessage(), true);//TODO Capire
            }
        }
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

    public void add(Server s){
        InfoServer temp = this.findLista(s);
        if(temp == null) this.mapInfoServer.put(s, new InfoServer());
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
    public CommandListManager getManager() {
        return this.manager;
    }

    public void setManager (CommandListManager manager) throws CommandException{
        if(manager == null) throw new CommandException("Errore, il gestore inserito è null!");
        this.manager = manager;
        this.factory.setManager(manager);
    }


}
