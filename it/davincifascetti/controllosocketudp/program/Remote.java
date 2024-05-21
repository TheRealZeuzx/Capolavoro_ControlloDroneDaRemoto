package it.davincifascetti.controllosocketudp.program;
import javax.swing.JFrame;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandInviaMsgClient;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Remote extends KeyAdapter {
    private Client client;
    private JFrame frame;
    // public Remote(Object client){
    //     if(client.getClass().isAssignableFrom(Client.class))
    //         throw new IllegalArgumentException("Il parametro deve essere di tipo client.");
    //     this.client = (Client) client; 
    // }

    // TODO se chiudi la finestra
    
    public Remote(Client client){
        this.client = client;
    }

    /**KeyEvent che viene chiamato all keyevent del jFrame
     * 
     */
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if(c == 'e'){
            frame.removeKeyListener(this);frame.dispose();
            this.client = null;
        }else{
            try {
                new CommandInviaMsgClient(this.client,"print " + String.valueOf(c)).execute();
            } catch (CommandException | ErrorLogException e1) {
                // TODO ADD ErrorLog (errorlog statico?)
                e1.printStackTrace();
            }
        }            
    }

        /**attiva la modalità telecomando (apre un JFrame per gli input e invia i msg al server)
     * 
     * @throws CommandException
     * @throws ErrorLogException
     */
    public void attiva() throws CommandException, ErrorLogException{
        this.frame = new JFrame("Premi per inviare");
        this.frame.setSize(500, 500);
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.frame.addKeyListener(this); 
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true); 
        this.frame.requestFocus(); this.frame.getKeyListeners();
        System.out.println("premi la tab per iniziare ad inviare ('e' per uscire)");
    }

    public boolean isActive(){
        if(this.frame != null && this.frame.isActive())
            return true;
        return false;
    }

    public boolean equals(Object o){
        try{
            if(o != null && this.getClient()!=null){
                if(Remote.class.isInstance(o))
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
