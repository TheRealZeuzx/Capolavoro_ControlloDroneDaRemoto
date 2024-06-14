package it.davincifascetti.controllosocketudp.program;

import java.io.IOException;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.drone.CommandNewDrone;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.user.User;

public class TerminaleDrone extends Terminal{
    private Video video = null;
    public TerminaleDrone(ErrorLog errorLog, GestoreClientServer business, User comandi, App app) throws CommandException, IOException, ErrorLogException {
        super(errorLog, business, comandi, app);
        this.video = new Video(this);
    }
    
    @Override
    protected void init(){
        super.init();
        try {
            new CommandNewDrone(this.getBusiness(), null, this).execute(); //appena avvio creo il drone 
        } catch (CommandException | ErrorLogException e) {
            
        }
    }

    @Override
    public void update(String eventType, Commandable responsabile) {
        if(responsabile == null){this.getCli().printError("Errore!");} //!gestire
        if(this.getCli().isAttivo(responsabile))this.getCli().print(responsabile.getClass().getSimpleName() + " dice che è successo questo: " + eventType); 
        if(responsabile.getDesc() == null){
            if(eventType.equals(Client.MESSAGE_SENT)) this.getCli().setLocked(true); 
        } 
        if(eventType.equals(Client.MESSAGE_RECEIVED) || eventType.equals(Client.SERVER_NO_RESPONSE)) this.getCli().setLocked(false); 
        
    }
    @Override
    public void update(String eventType, Commandable responsabile,Commandable target) {
        if(responsabile == null){this.getCli().printError("Errore!");} //!gestire
        if(this.getCli().isAttivo(responsabile))this.getCli().print(responsabile.getClass().getSimpleName() + " dice che è successo questo: " + eventType); 
        //TODO quando elimino un client o server devo rimuoverlo dalle liste componenti esempio elimino un client, devo rimuoverlo dalla lista di gestoreRemote
        if(eventType == GestoreClientServer.SERVER_ADDED && target.getDesc() != null && target.getDesc().equals("video")){
            //! be. .. usiamo una api esterna per il server, quindi il "server_added" non ha molto senso..
            //! il target è inutile allo scopo del video fino a che "video" sarà implementato con api esterne che si connettono per conto proprio
            this.getVideo().start();
        }
    }

    @Override
    public void update(byte[] buffer, int lung, Commandable commandable) {
        if(commandable == null){this.getCli().printError("Errore, qualcosa è andato storto!");} //!gestire   
        //messaggio ricevuto in dal server da un client (ricevuto dal server)
        if(ServerThread.class.isInstance(commandable)){
            //in base alla descrizione decido come gestire es: getDesc.equals("video") --> aggiorno il video
            if(commandable.getDesc() == null){
                try {
                    this.getGestoreRisposte().gestisciRisposta(buffer, lung, (ServerThread)commandable);
                } catch (CommandException e) {
                    this.getCli().printError(e.getMessage());
                } catch (ErrorLogException e) {
                    this.getCli().printError(e.getMessage());
                    this.fileErrorLog(e.getMessage());
                }
            }else if(commandable.getDesc().equals("video")){
                this.getVideo().updateVideo(buffer, lung); //!quando andrò a creare il server che riceve il video, assegnerò come desc: video =/
                //! sicuro? considerata la nuova implementazione del video, non servirà aggiornare il video manualmente.
            }
        //messaggio ricevuto in risposta ad una richiesta del client (ricevuto dal client)
        }else if(Client.class.isInstance(commandable)){
            if(commandable.getDesc() == null){
                try {
                    this.getGestoreRisposte().gestisciRisposta(buffer, lung, (Client)commandable);
                } catch (CommandException e) {
                    this.getCli().printError(e.getMessage());
                } catch (ErrorLogException e) {
                    this.getCli().printError(e.getMessage());
                    this.fileErrorLog(e.getMessage());
                }
            }
        }
    }

    @Override
    public void destroy(){
        super.destroy();
        this.video.destroy();
    }
    public Video getVideo(){return this.video;}

}
