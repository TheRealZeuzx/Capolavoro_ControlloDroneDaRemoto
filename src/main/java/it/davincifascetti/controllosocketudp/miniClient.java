package it.davincifascetti.controllosocketudp;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;




public class miniClient implements Runnable{
    private String identifier;
    private InetAddress telloIP;
    private int telloPort;
    private DatagramSocket socket;
    private Thread listening;
    private static boolean vero = false;
    


    public miniClient(String telloIP, int telloPort, String identifier) throws UnknownHostException, SocketException{
        this.telloIP = InetAddress.getByName(telloIP);
        this.telloPort = telloPort;
        this.identifier = identifier;
        this.socket = new DatagramSocket(telloPort);
        this.listening = new Thread(this);
        this.listening.start();

    }

    

    public void run(){
        

            File imgPath = new File("image.png");
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(imgPath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            WritableRaster raster = bufferedImage .getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
            System.err.println("|" + data.getSize());
            DatagramPacket video = new DatagramPacket(data.getData(),data.getSize(),this.telloIP,this.telloPort);
            try {
                this.socket.send(video);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                       
        
    
    }


    public static void main(String[] args) {
        try{
            
            miniClient invioVideo = new miniClient("localhost", 11111, "InviaVideo");
            miniServer streamVideo = new miniServer("StreamVideo","11111", "localhost");
            streamVideo.iniziaAscolto();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
