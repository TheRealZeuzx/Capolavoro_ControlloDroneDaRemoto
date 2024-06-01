package it.davincifascetti.controllosocketudp.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.davincifascetti.controllosocketudp.command.Commandable;

public class EventManagerCommandable {
    private Map<String, ArrayList<EventListenerCommandable>> listenerCommandable = new HashMap<>();
    private ArrayList<EventListenerRicezioneBuffer> listenerRicezioneBuffer = new ArrayList<>();

    public EventManagerCommandable() {
        super();
    }

    /*metodi per EventListenerCommandable*/

    public EventManagerCommandable(String... operations) {
        for (String operation : operations) {
            this.listenerCommandable.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, EventListenerCommandable listener) {
        ArrayList<EventListenerCommandable> users = listenerCommandable.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(String eventType, EventListenerCommandable listener) {
        ArrayList<EventListenerCommandable> users = listenerCommandable.get(eventType);
        users.remove(listener);
    }

    public void notify(String eventType, Commandable commandable){
        ArrayList<EventListenerCommandable> users = listenerCommandable.get(eventType);
        for (EventListenerCommandable listener : users) {
            listener.update(eventType, commandable);
        }
    }


    /*metodi per EventListenerRicezioneBuffer*/

    

    public void subscribe(EventListenerRicezioneBuffer listener) {
        listenerRicezioneBuffer.add(listener);
    }

    public void unsubscribe(EventListenerRicezioneBuffer listener) {
        listenerRicezioneBuffer.remove(listener);
    }

    public void notify(byte[] buffer, int length,Commandable commandable) {
        for (EventListenerRicezioneBuffer listener : this.listenerRicezioneBuffer) {
            listener.update(buffer,length,commandable);
        }
    }


}



