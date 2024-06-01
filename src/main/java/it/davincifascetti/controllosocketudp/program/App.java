package it.davincifascetti.controllosocketudp.program;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.program.user.User;

public class App {

    private GestoreClientServer business;
    private Ui ui;// la ui avrà anche un componente per gestire le richieste al server e le sue risposte

    public App(String errorLogPath,User comandi) throws NullPointerException,CommandableException, CommandException{
        if(errorLogPath == null || errorLogPath.isBlank()) throw new NullPointerException("Errore, il path è null oppure vuoto!");
        this.business = new GestoreClientServer();
        this.ui = new Terminal(new ErrorLog(errorLogPath),business,comandi);
    }

    public void start() throws CommandException{
        this.ui.start();
    }
}
