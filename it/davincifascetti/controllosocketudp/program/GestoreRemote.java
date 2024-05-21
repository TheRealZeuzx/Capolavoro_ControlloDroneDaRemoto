package it.davincifascetti.controllosocketudp.program;

import java.util.Vector;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

public class GestoreRemote {
    //// TO DO capire dove inserire il gestore remote 
    // Fixed. Messo riferimento in remote al suo gestore (musso, odiami, era la scelta più facile per il testing....)
    // TODO capire come gestire il caso in cui il client venga eliminato (e il remote sia ancora aperto)
    // ancora to do? dipende se poi vuoi modificare la struttura e trovare un modo di collegare remote e gestoreRemote in qualche altro modo, altrimenti è fixato. vedi tu
    private Vector<Remote> listaRemote = null;
    
    public GestoreRemote() {
        listaRemote = new Vector<Remote>();
    }

    public void modTelecomando(Client c) throws CommandException,ErrorLogException{
        Remote temp = this.findRemote(c);
        if(temp==null){
            temp = new Remote(c);
            this.listaRemote.add(temp);
        }
        if(!temp.isActive())
            temp.attiva();
    }

    private Remote findRemote(Client c) throws CommandException,ErrorLogException{
        int pos = this.listaRemote.indexOf(new Remote(c));
        if(pos == -1)
            return null;
        return this.listaRemote.get(pos);
    }

}
