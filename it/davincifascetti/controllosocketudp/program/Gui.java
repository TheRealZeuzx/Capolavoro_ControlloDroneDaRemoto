package it.davincifascetti.controllosocketudp.program;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.davincifascetti.controllosocketudp.command.CommandListManager;

public class Gui{
    private JFrame frame;
    private JPanel panelVideo; 
    private Graphics video;
    public Gui(){



        this.frame = new JFrame();
        this.frame.add(panelVideo);
        while(true){
            this.frame.paint(this.video);
        }
    }
    


}

