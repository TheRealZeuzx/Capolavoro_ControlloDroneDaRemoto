package it.davincifascetti.controllosocketudp.program;
import it.davincifascetti.controllosocketudp.command.Commandable;

public interface EventListenerCommandable {
    public abstract void update(String eventType,Commandable responsabile);
    /**
     * 
     * @param eventType
     * @param responsabile chi ha sollevato l'evento
     * @param target chi è il target dell'evento. ad esempio CLIENT_ADDED , responsabile = GestoreClientServer, target = oggetto Client aggiunto 
     */
    public abstract void update(String eventType,Commandable responsabile,Commandable target);
}