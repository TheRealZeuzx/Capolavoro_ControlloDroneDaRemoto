package it.davincifascetti.controllosocketudp.program;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;

public interface EventListenerCommandable {
    void update(String eventType,Commandable commandable) throws CommandException;
}