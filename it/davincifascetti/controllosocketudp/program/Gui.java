package it.davincifascetti.controllosocketudp.program;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

public class Gui extends KeyAdapter {
    private JFrame frame;
    private JPanel panel;
    private Remote Telecomando; 

    public static void main(String[] args) {
        Gui x = new Gui();
        x.attivaRemote();
    }

    /**KeyEvent che viene chiamato all keyevent del jFrame
     * 
     */
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if(c == 'e'){ 
            frame.removeKeyListener(this);frame.dispose();
        }      
    }

        /**attiva la modalità telecomando (apre un JFrame per gli input e invia i msg al server)
     * 
     * @throws CommandException
     * @throws ErrorLogException
     */
    public void attivaRemote(){
        creaWindow();
        creaPulsanti();



        System.out.println("premi la tab per iniziare ad inviare ('e' per uscire)");
    }

    private void creaWindow(){
        this.frame = new JFrame("Premi per inviare");
        this.frame.setSize(1200, 500);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true); 
        this.frame.requestFocus(); 
        this.frame.setResizable(false);
        this.panel = new JPanel();
        this.panel.setBackground(Color.GRAY);
        this.panel.setLayout(new GridLayout(2,2));
        this.frame.add(this.panel, BorderLayout.CENTER);
        this.panel.addKeyListener(this); 
    }

    private void creaPulsanti(){
        JButton button = new JButton("button1");
        JButton button2 = new JButton("button2");
        JButton button3 = new JButton("button3");
        JButton button4 = new JButton("button4");
        this.panel.add(button);
        this.panel.add(button2);
        this.panel.add(button3);
        this.panel.add(button4);
    }

    public boolean isActive(){
        if(this.frame != null && this.frame.isActive())
            return true;
        return false;
    }

}

