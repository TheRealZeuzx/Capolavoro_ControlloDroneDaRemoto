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
import it.davincifascetti.controllosocketudp.command.Commandable;

public class Video extends Component implements EventListenerRicezioneBuffer{

    private JFrame frame = null;

    public Video(Ui ui) throws CommandException{
        this.setUi(ui);
    }
    public Video(){super();}


    @Override
    public void update(byte[] buffer, int lung,Commandable commandable) {

        if(this.frame == null){
            this.frame =  new JFrame("FPV Drone");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }


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
    
}
