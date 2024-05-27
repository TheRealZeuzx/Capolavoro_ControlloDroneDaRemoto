import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

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

    public static void main(String[] args) {
        try{
            miniClient telloDrone = new miniClient("192.168.10.1", 8889, "Drone Tello");
            miniClient streamVideo = new miniClient("0.0.0.0",11111, "StreamVideo");
            // Abilita comandi
            byte[] bufferOUT = new byte[1024];
            String msg = "command";
            bufferOUT = msg.getBytes();
            DatagramPacket remark2 = new DatagramPacket(bufferOUT,bufferOUT.length,telloDrone.telloIP,telloDrone.telloPort);
            telloDrone.socket.send(remark2);
            // Abilita video
            /*  

            */
            msg = "streamon";
            bufferOUT = msg.getBytes();
            DatagramPacket remark5 = new DatagramPacket(bufferOUT,bufferOUT.length,telloDrone.telloIP,telloDrone.telloPort);
            telloDrone.socket.send(remark2);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        byte[] bufferIN = new byte[1024];
        DatagramPacket ricevuto = new DatagramPacket(bufferIN,bufferIN.length);
        String msgRicevuto = null;
        try {
            socket.receive(ricevuto);
            msgRicevuto = new String(ricevuto.getData());
            msgRicevuto = msgRicevuto.substring(0, ricevuto.getLength());
            System.out.println(this.identifier + " - Server response: " + msgRicevuto);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(this.identifier == "StreamVideo" && !vero){
            try {
                ByteArrayInputStream inStreambj = new ByteArrayInputStream(bufferIN);
                BufferedImage newImage = ImageIO.read(inStreambj);
                ImageIO.write(newImage, "jpg", new File("outputImage.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Thread getListening() {
        return listening;
    }

    public void setListening(Thread listening) {
        this.listening = listening;
    }

    public InetAddress getTelloIP() {
        return telloIP;
    }

    public void setTelloIP(InetAddress telloIP) {
        this.telloIP = telloIP;
    }


    public int getTelloPort() {
        return telloPort;
    }

    public void setTelloPort(int telloPort) {
        this.telloPort = telloPort;
    }


    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

}


/*

 */