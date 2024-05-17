package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Terminal;

/**
 * GENERALE
    Interface Commandable.
    Interfaccia implementata da tutte le classi che hanno a disposizione un terminale.
    @author Tommaso Mussaldi
    @version 1.0
 */
public interface Commandable {
    /**
        Metodo astratto dedicato a far partire il terminale con l'istanza dell oggetto chiamante.
        @throws CommandException se il terminale non riesce a partire.
    */
    public void startTerminal() throws CommandException;
    public Terminal<? extends Commandable> getTerminal();
}