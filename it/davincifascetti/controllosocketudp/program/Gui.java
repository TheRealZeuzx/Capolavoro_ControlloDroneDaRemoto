package it.davincifascetti.controllosocketudp.program;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Gui extends KeyAdapter {
    private JFrame frame;
    private Remote Telecomando; 

    /**KeyEvent che viene chiamato all keyevent del jFrame
     * 
     */
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if(c == 'e'){ 
            frame.removeKeyListener(this);frame.dispose();
        }else{
            if(c == 'a'){
                frame.object
                Telecomando.commandInput("sinistraDrone");
            }
            if(c == 'AdelController'){ "a"
                Telecomando.commandInput("sinistraDrone");
            }
            Factory.commandInput("a","CommandSinistra")
            Factory.commandInput("d","CommandSinistra")
            Factory.commandInput("w","CommandSinistra")
            Factory.commandInput("s","CommandSinistra")
        }            
    }

        /**attiva la modalità telecomando (apre un JFrame per gli input e invia i msg al server)
     * 
     * @throws CommandException
     * @throws ErrorLogException
     */
    public void attivaRemote() throws CommandException, ErrorLogException{
        if(this.client == null) throw new CommandException("Errore, il client è null!");
        if(!this.client.socketIsSet()) throw new CommandException("Errore, prima devi impostare una socket!");
        this.frame = new JFrame("Premi per inviare");
        this.frame.setSize(500, 500);
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.frame.addKeyListener(Remote); 
        this.frame.addKeyListener(Terminale); 
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

}

