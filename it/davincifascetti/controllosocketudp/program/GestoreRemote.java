package it.davincifascetti.controllosocketudp.program;
import java.util.Vector;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

public class GestoreRemote implements EventListenerCommandable,Component{

    private Vector<Remote> listaRemote = null;
    private Ui riferimentoUi;
    public GestoreRemote(Ui ui) throws CommandException {
        listaRemote = new Vector<Remote>();
        this.setUi(ui);
    }

    public void modTelecomando(Client c) throws CommandException,ErrorLogException{
        Remote temp = this.findRemote(c);
        if(temp==null){
            temp = new Remote(c,this);
            this.listaRemote.add(temp);
            if(!temp.isActive())
                temp.attiva();
        }
        
    }

    private Remote findRemote(Client c) throws CommandException{
        int pos = this.listaRemote.indexOf(new Remote(c,this));
        if(pos == -1)
            return null;
        return this.listaRemote.get(pos);
    }

    @Override
    public void update(String eventType, Commandable commandable){
        Remote temp = null;
        if(!Client.class.isInstance(commandable)) return;
        try {
            temp = this.findRemote(((Client)commandable));
        } catch (CommandException e) {
            System.out.println("Errore, qualcosa è andato storto | " + e.getMessage());
        }
        if(temp == null)return;
        temp.destroy();
    }

    public void remove(Remote r){
        if(r == null) return;
        this.listaRemote.remove(r);
    }

    @Override
    public CommandListManager getManager() {
        return null;
    }

    @Override
    public void setManager(CommandListManager manager) throws CommandException {
        return;
    }

    @Override
    public void setUi(Ui ui) throws CommandException {
        if(ui == null) throw new CommandException("Errore, la UI passata è null!");
        this.riferimentoUi = ui;
    }


}