package it.davincifascetti.controllosocketudp.program;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;

public class App {
    private GestoreClientServer business;
    private Ui ui;

    public App(String errorLogPath) throws NullPointerException,CommandableException, CommandException{
        if(errorLogPath == null || errorLogPath.isBlank()) throw new NullPointerException("Errore, il path è null oppure vuoto!");
        this.business = new GestoreClientServer();
        this.ui = new Terminal(new ErrorLog(errorLogPath),business);
    }

    public void start() throws CommandException{
        this.ui.start();
    }
}
