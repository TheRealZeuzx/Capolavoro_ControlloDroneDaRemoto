package it.davincifascetti.controllosocketudp.program;
import java.util.Vector;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

public class GestoreRemote implements EventListenerCommandable{
    // TODO capire dove inserire il gestore remote
    // TODO capire come gestire il caso in cui il client venga eliminato (e il remote sia ancora aperto)
    // TODO per poter gestire il caso in cui il client venga eliminato possiamo implementare l'observer design pattern.
    private Vector<Remote> listaRemote = null;
    public GestoreRemote() {
        listaRemote = new Vector<Remote>();
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
    public void update(String eventType, Commandable commandable) throws CommandException {
        Remote temp = null;
        if(!Client.class.isInstance(commandable)) return;
        temp = this.findRemote(((Client)commandable));
        if(temp == null)return;
        temp.destroy();
    }

    public void remove(Remote r){
        if(r == null) return;
        this.listaRemote.remove(r);
    }


}