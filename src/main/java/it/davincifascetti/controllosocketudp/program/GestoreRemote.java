package it.davincifascetti.controllosocketudp.program;
import java.io.IOException;
import java.util.Vector;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

public class GestoreRemote extends Component {

    private Vector<Remote> listaRemote = null;
    public GestoreRemote() throws CommandException {
        listaRemote = new Vector<Remote>();
    }
    //TODO mettere i comandi specificati in un file xml in modo da fare che i comandi sono nella UI, ogni componente può avere comandi es CLI e Remote sarà poi ogni componente a cercare se stesso nella lista comandi che sta in UI
    public GestoreRemote(Ui ui) throws CommandException {
        this();
        this.setUi(ui);
    }

    public void modTelecomando(Client c) throws CommandException,ErrorLogException, IOException{
        ((Terminal)this.getUi()).getCli().setLocked(true);
        if(c == null) throw new CommandException("Errore, il client calling è null!");
        Remote temp = this.findRemote(c);
        if(temp==null){
            temp = new Remote(c,this);
            this.listaRemote.add(temp);
            if(!temp.isActive()){   
                temp.attiva();
            }
        }
        
    }

    public void destroy(Remote remote){
        remote.destroy();
        this.remove(remote);
        ((Terminal)this.getUi()).getCli().setLocked(false);
    }

    private Remote findRemote(Client c) throws CommandException{
        int pos = this.listaRemote.indexOf(new Remote(c,this));
        if(pos == -1)
            return null;
        return this.listaRemote.get(pos);
    }

    // @Override
    // public void update(String eventType, Commandable commandable){
    //     Remote temp = null;
    //     if(!Client.class.isInstance(commandable)) return;
    //     try {
    //         temp = this.findRemote(((Client)commandable));
    //     } catch (CommandException e) {
    //         System.out.println("Errore, qualcosa è andato storto | " + e.getMessage());
    //     }
    //     if(temp == null)return;
    //     temp.destroy();
    // }

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



}