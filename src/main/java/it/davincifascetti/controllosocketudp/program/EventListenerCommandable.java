package it.davincifascetti.controllosocketudp.program;
import it.davincifascetti.controllosocketudp.command.Commandable;

public interface EventListenerCommandable {
    public abstract void update(String eventType,Commandable commandable);
}