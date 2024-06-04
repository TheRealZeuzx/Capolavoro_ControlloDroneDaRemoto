package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.EventManagerCommandable;

/**
 * GENERALE
    Interface Commandable.
    @author Tommaso Mussaldi
    @version 1.0
 */
public interface Commandable {
    /**
        Metodo astratto dedicato a far partire il terminale con l'istanza dell oggetto chiamante.
        @throws CommandException se il terminale non riesce a partire.
    */
    public abstract EventManagerCommandable getEventManager();
    public abstract String getDesc();
}