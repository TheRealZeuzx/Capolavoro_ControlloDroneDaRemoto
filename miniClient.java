import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
    private JFrame frame = null;


    public miniClient(String telloIP, int telloPort, String identifier) throws UnknownHostException, SocketException{
        this.telloIP = InetAddress.getByName(telloIP);
        this.telloPort = telloPort;
        this.identifier = identifier;
        this.socket = new DatagramSocket(telloPort);
        this.listening = new Thread(this);
        this.listening.start();
        if(this.frame == null && identifier.equalsIgnoreCase("StreamVideo")){
            this.frame =  new JFrame("Image Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        try{
            miniClient telloDrone = new miniClient("192.168.10.1", 8889, "Drone Tello");
            miniClient streamVideo = new miniClient("0.0.0.0",11111, "StreamVideo");
            // Abilita comandi
            byte[] bufferOUT = new byte[6220800];
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
            telloDrone.socket.send(remark5);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
        byte[] bufferIN = new byte[6220800];
        DatagramPacket ricevuto = new DatagramPacket(bufferIN,bufferIN.length);
        String msgRicevuto = null;
        try {
            socket.receive(ricevuto);
            msgRicevuto = new String(ricevuto.getData());
            msgRicevuto = msgRicevuto.substring(0, ricevuto.getLength());
            // System.out.println(this.identifier + " - Server response: " + msgRicevuto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // for(int i = 0;i<ricevuto.getData().length;i++){
        //     System.out.print(ricevuto.getData()[i]);
        //     System.out.println("");
        // }

        if(this.identifier.equalsIgnoreCase("StreamVideo")){
            try {
                System.out.println(this.identifier + " - Server response: " + ricevuto.getData());
                ByteArrayInputStream inStreambj = new ByteArrayInputStream(ricevuto.getData());
                BufferedImage image = ImageIO.read(inStreambj);
                // BufferedImage image = ImageIO.read(new File("image.png"));
                if(image != null){
                    JLabel imageLabel = new JLabel(new ImageIcon(image));
                    frame.add(imageLabel);
                    frame.pack();
                    frame.setVisible(true);
                }else {System.out.println("image = null =/");}




            } catch (IOException e) {
                e.printStackTrace();
            }
        }}
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