package it.davincifascetti.controllosocketudp.program;
import javax.swing.JFrame;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandInviaMsgClient;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

import java.awt.event.KeyAdapter;


public class Remote  {
    private Client client;

    public Remote(Client client) throws CommandException{
        if(client == null) throw new CommandException("Errore, il client passato è null!");
        this.client = client;
    }

    public void commandInput(String Input){
        // HASHMAP CON INPUT = COMANDO
    }

    public boolean equals(Object o){
        try{
            if(o != null && this.getClient()!=null){
                if(Remote.class.isInstance(o))
                    if(((Remote)o).getClient() != null)
                        if(((Remote)o).getClient().equals(this.getClient()))
                            return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
        
    }   

	public Client getClient() {
        return this.client; 
    }
}
