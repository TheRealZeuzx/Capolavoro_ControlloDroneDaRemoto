package it.davincifascetti.controllosocketudp.program;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandListManager;

public class Video extends Component {
    public static final int TIME_BEFORE_CLOSING_FRAME = 2000;
    private final AtomicBoolean INIT = new AtomicBoolean();
    private JFrame frame = null;
    private JLabel label = null;

    public Video(Ui ui) throws CommandException{
        this.setUi(ui);
        
    }
    public Video(){super();
        this.frame = new JFrame("FPV Drove");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.label = new JLabel();
    }

    
    /**Uso questa classe per aggiornare il frame del video quando mi arriva il pacchetto
     * 
     * @param buffer
     * @param lung
     */
    public void updateVideo(byte[] buffer, int lung ) {
        try {
            synchronized(this.frame){
                this.frame.setVisible(true);
                this.decodeH264Frame(buffer);
                this.startTimer();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void startTimer() throws InterruptedException{        
        this.wait(TIME_BEFORE_CLOSING_FRAME);
        this.frame.setVisible(false);
    }
    // public void updateVideo(byte[] buffer, int lung ) {
    //     System.out.println("nuovo frame!");
    //     if(this.frame == null){
    //         this.frame =  new JFrame("FPV Drone");
    //         frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //         frame.setSize(400, 300);
    //         frame.setLocationRelativeTo(null);
    //         frame.setVisible(true);
    //     }


    //     ByteArrayInputStream inStreambj = new ByteArrayInputStream(buffer);
    //     BufferedImage image = null;
    //     try {
    //         image = ImageIO.read(inStreambj);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     if(image != null){
    //         JLabel imageLabel = new JLabel(new ImageIcon(image));
    //         frame.add(imageLabel);
    //         frame.pack();
    //         frame.setVisible(true);
    //     }else {System.out.println("image==null |  =/");}
    // }
    
    @Override
    public CommandListManager getManager() {
        return null;
    }
    @Override
    public void setManager(CommandListManager manager) throws CommandException{
        return ;
    }

    public void decodeH264Frame(byte[] h264Frame) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(h264Frame);

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(byteArrayInputStream);
        grabber.setFormat("h264");
        grabber.setOption("probesize", "5000000");
        grabber.setOption("analyzeduration", "5000000");

        grabber.start();

        Frame frame = grabber.grabImage();
        if (frame == null) {
            throw new RuntimeException("Could not decode video frame.");
        }

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage image = converter.getBufferedImage(frame);

        grabber.stop();
        grabber.release();
        if (image != null) {
            label.setIcon(new ImageIcon(image));
            this.frame.pack(); // Ridimensiona il frame per adattarsi all'immagine
            System.out.println("Immagine impostata nel JLabel");
        } else {
            System.out.println("Nessuna immagine disponibile");
        }

        // Aggiorna il frame per renderlo visibile
        this.frame.repaint();
    }

    public void destroy(){
        super.destroy();
        if(this.frame != null && this.frame.isActive())this.frame.dispose();
        this.frame = null;
        this.label = null;

    }
    
}
