package it.davincifascetti.controllosocketudp.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;

public class EventManagerCommandable {
    private Map<String, ArrayList<EventListenerCommandable>> listeners = new HashMap<>();

    public EventManagerCommandable(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, EventListenerCommandable listener) {
        ArrayList<EventListenerCommandable> users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(String eventType, EventListenerCommandable listener) {
        ArrayList<EventListenerCommandable> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(String eventType, Commandable commandable) throws CommandException {
        ArrayList<EventListenerCommandable> users = listeners.get(eventType);
        for (EventListenerCommandable listener : users) {
            listener.update(eventType, commandable);
        }
    }
}