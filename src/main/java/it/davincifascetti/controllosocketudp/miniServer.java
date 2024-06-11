package it.davincifascetti.controllosocketudp;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.FileLogger;
import it.davincifascetti.controllosocketudp.program.Video;
/**classe server, crea un thread che si occupa della ricezione e uno separato (ServerThread) che si occupa di rispondere
 * 
 * @throws CommandableExceptio errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class miniServer implements Runnable{
    
    public static final int LunghezzaBuffer = 6200000;
    private DatagramSocket socket;
    private String nome;
    private int porta;
    private boolean statoAttivo=false;
    private Thread threadAscolto = null;
    private String ip;
    private Video video = null;
    /**se uso quest costruttore, posso attivare il server
     * 
     * @param nomeClient nome del server
     * @param porta porta locale su cui aprire il socket
     * @param t riferimento terminale che verra usato da questo server
     * @throws CommandableException
     * @throws ErrorLogException
     * @throws UnknownHostException 
     */
    public miniServer(String nomeServer, String porta,String ip) throws CommandableException,ErrorLogException, UnknownHostException{
        this.setPorta(porta);
        this.nome = nomeServer;
        this.ip = ip;
        this.video = null;
        try {
            this.socket = new DatagramSocket(this.porta,InetAddress.getByName(ip));
        } catch (SocketException e) {
            throw new ErrorLogException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            miniServer serv = new miniServer("s1", "11111","0.0.0.0");
            serv.iniziaAscolto();
        } catch (UnknownHostException | CommandableException | ErrorLogException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**implementa la logica di ricezione e attiva il thread di risposta
     * 
     */
    @Override
    public void run(){
        StringBuilder packetData = new StringBuilder();        
        if(!this.isAttivo())return;
        byte[] bufferIN = new byte[miniServer.LunghezzaBuffer];
        while(this.isAttivo()){
            DatagramPacket pacchetto = new DatagramPacket(bufferIN, miniServer.LunghezzaBuffer);
            try {
                this.socket.receive(pacchetto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
        if(!this.socket.isClosed())this.socket.close();
    }

    /**permette di avviare il server (crea e avvia il thread)
     * 
     * @throws CommandableException
     * @throws ErrorLogException 
     */
    public void iniziaAscolto()throws CommandableException, ErrorLogException{
        if(!this.socketIsSet()) throw new CommandableException("Errore, la socket è null non può essere avviato, imposta una porta prima");
        if(this.socket.isClosed())
            try {
                this.socket = new DatagramSocket(this.porta);
            } catch (SocketException e) {
                throw new ErrorLogException(e.getMessage());
            }
        this.statoAttivo = true;
        this.video = new Video();
        if(this.threadAscolto == null)this.threadAscolto = new Thread(this);
        if(!this.threadAscolto.isAlive())this.threadAscolto.start(); 
    }

    /**permette di terminare l'ascolto del server (interrompe il thread creato in iniza ascolto)
     * chiude la socket
     */
    public void terminaAscolto(){
        this.statoAttivo = false;
        if(this.socket != null){
            if(!this.socket.isClosed())this.socket.close();
        }
        if(this.threadAscolto != null ) this.threadAscolto.interrupt();
        this.threadAscolto = null;
        this.video.destroy();
        this.video = null;
    }

    @Override
    public String toString() {
        return "Name: " + this.getNome() + "\t" + (this.socket == null?"Port: - ":("Port: "+this.getPorta())) + "\tStatus: "+ (this.isAttivo() ? "attivo" : "disattivo");
    }


    public boolean isAttivo(){return this.statoAttivo;}

    public String getNome(){return this.nome;}
    /**imposta il nome (deve avere almeno una lettera, può contenere numeri, lettere e _ min 1 carattere e max 18)
     * 
     * @param nome nuovo nome del server
     * @throws CommandableException
     */
    public void setNome(String nome) throws CommandableException{
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_0-9]{1,18}$"))
            this.nome = nome;
        else 
            throw new CommandableException("Errore, il nome '"+nome+"' inserito non è valido (deve contenere almeno una lettera, può contenere numeri da 0 a 9, lettere maiusc e minusc e '_')");
    }
    /**imposta la porta locale
     * 
     * @param port porta come Stringa perchè l'utente potrebbe scrivere una lettera e mandare in crash
     * @throws CommandableException
     */
    private void setPorta(String port)throws CommandableException{
        int p;
        try{
            p = Integer.valueOf(port);
        }catch(NumberFormatException e){
            throw new CommandableException("Errore, '" + port + "' non è un numero, specifica il numero della porta");
        }
        if(p < 0 || p > 65535)throw new CommandableException("Errore, la porta inserita non è valida (0-65535)");
        else this.porta = p;
    }
    public int getPorta(){return this.porta;}

    /**si occupa di terminare l'ascolto,modificare il socket e se era attivo riattivare la socket
     * 
     * @param porta nuova porta locale
     * @throws CommandableException
     * @throws ErrorLogException
     * @throws UnknownHostException 
     */
    public void setSocket(String porta,String ip) throws CommandableException, ErrorLogException, UnknownHostException{
        boolean wasActive = this.isAttivo();
        if(this.isAttivo())
            this.terminaAscolto();
        this.setPorta(porta);
        this.ip = ip;
        try {
            this.socket = new DatagramSocket(this.porta,InetAddress.getByName(ip));
        } catch (SocketException e) {
            throw new ErrorLogException(e.getMessage());
        }
        if(wasActive)this.iniziaAscolto();
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(miniServer.class.isInstance(o)){
            return ((miniServer)o).getNome().equals(this.getNome());
        }
        return false;
    }


    public boolean socketIsSet(){
        if(this.socket == null) return false;
        return true;
    }

}
