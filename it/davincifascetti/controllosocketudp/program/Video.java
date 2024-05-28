package it.davincifascetti.controllosocketudp.program;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandListManager;

public class Video implements Component,EventListenerRicezioneBuffer{

    private JFrame frame = null;
    private Ui riferimentoUi;

    public Video(Ui ui) throws CommandException{
        this.setUi(ui);
        this.frame =  new JFrame("Image Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    @Override
    public void update(byte[] buffer, int lung) {
        ByteArrayInputStream inStreambj = new ByteArrayInputStream(buffer);
        BufferedImage image = null;
        try {
            image = ImageIO.read(inStreambj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(image != null){
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            frame.add(imageLabel);
            frame.pack();
            frame.setVisible(true);
        }else {System.out.println("image==null |  =/");}
    }
    
    @Override
    public CommandListManager getManager() {
        return null;
    }
    @Override
    public void setManager(CommandListManager manager) throws CommandException{
        return ;
    }

    @Override
    public void setUi(Ui ui) throws CommandException {
        if(ui == null) throw new CommandException("Errore, la UI passata è null!");
        this.riferimentoUi = ui;
    }
}
