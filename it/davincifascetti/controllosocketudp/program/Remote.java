package it.davincifascetti.controllosocketudp.program;
import javax.swing.JFrame;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandInviaMsgClient;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Remote extends KeyAdapter{
    private Client client;
    private JFrame frame;
    private Character tastoPrecedente;
    private GestoreRemote gestoreRemote;

    
    public Remote(Client client,GestoreRemote gest) throws CommandException{
        if(client == null) throw new CommandException("Errore, il client è null!");
        if(gest == null) throw new CommandException("Errore, il gestore remote è null!");
        this.gestoreRemote = gest;
        this.client = client;
        this.frame = new JFrame("Premi per inviare");
        this.frame.setEnabled(false);
    }

    /**KeyEvent che viene chiamato all keyevent del jFrame
     * 
     */
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if(c == 'e'){
            this.destroy();
        }
        try {
            if(tastoPrecedente == null || !tastoPrecedente.equals(Character.valueOf(c))){
                tastoPrecedente = Character.valueOf(c);
                new CommandInviaMsgClient(this.client,"print " + String.valueOf(c)).execute();
            }
        } catch (CommandException e1) {
            
        } catch (ErrorLogException e1) {
            
        }
    }

        /**attiva la modalità telecomando (apre un JFrame per gli input e invia i msg al server)
     * 
     * @throws CommandException
     * @throws ErrorLogException
     */
    public void attiva() throws CommandException, ErrorLogException{
        Terminal.setBloccato(true);
        this.frame.setEnabled(true);
        this.frame.requestFocus(); 
        this.frame.setSize(500, 500);
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.frame.addKeyListener(this); 
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true); 
        System.out.println("premi la tab per iniziare ad inviare ('e' per uscire)");
    }

    public boolean isActive(){
        return this.frame.isActive();
    }

    public void destroy(){
        this.frame.removeKeyListener(this);
        this.frame.dispose();
        this.gestoreRemote.remove(this);
        Terminal.setBloccato(false);
        this.client = null;
    }

    public boolean equals(Object o){
        if(o != null && this.getClient()!=null){
            if(Remote.class.isInstance(o))
                if(((Remote)o).getClient() != null)
                    if(((Remote)o).getClient().equals(this.getClient()))
                        return true;
        }
        return false;   
    }   

	public Client getClient() {
        return this.client; 
    }
}