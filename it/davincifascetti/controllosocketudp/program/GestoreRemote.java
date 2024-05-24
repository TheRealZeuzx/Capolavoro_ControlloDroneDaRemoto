package it.davincifascetti.controllosocketudp.program;

import java.util.Stack;
import java.util.Vector;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

public class GestoreRemote {
    // TODO capire dove inserire il gestore remote
    // TODO capire come gestire il caso in cui il client venga eliminato (e il remote sia ancora aperto)
    private Stack<Remote> listaRemote = null;
    
    public GestoreRemote() {
        listaRemote = new Stack<Remote>();
    }

    public void modTelecomando(Client c) throws CommandException,ErrorLogException{
        Remote temp = this.findRemote(c);
        if(temp==null){
            temp = new Remote(c);
            this.listaRemote.add(temp);
            if(!temp.isActive())
                temp.attiva(this);
        }
            
        
    }

    private Remote findRemote(Client c){
        int pos = this.listaRemote.indexOf(new Remote(c));
        if(pos == -1)
            return null;
        return this.listaRemote.get(pos);
    }

}