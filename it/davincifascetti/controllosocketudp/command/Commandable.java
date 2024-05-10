package it.davincifascetti.controllosocketudp.command;

import java.util.concurrent.atomic.AtomicBoolean;

/**
    Interface Commandable.
    Interfaccia implementata da tutte le classi che hanno a disposizione un terminale.
    @author Mussaldi Tommaso, Bonfiglio Mattia
    @version 1.0
 */
public interface Commandable {
    /**
        Metodo astratto dedicato a far partire il terminale con l'istanza dell oggetto chiamante.
        @throws CommandException se il terminale non riesce a partire.
    */
    public void startTerminal() throws CommandException;
    public static final CommandList comandi = new CommandList();
}
