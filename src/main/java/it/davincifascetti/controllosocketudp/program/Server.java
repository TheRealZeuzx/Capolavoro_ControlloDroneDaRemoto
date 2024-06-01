package it.davincifascetti.controllosocketudp.program;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**classe server, crea un thread che si occupa della ricezione e uno separato (ServerThread) che si occupa di rispondere
 * 
 * @throws CommandableExceptio errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class Server implements Runnable,Commandable{
    
    public static final int LunghezzaBuffer = 1024;
    private DatagramSocket socket;
    private String nome;
    private int porta;
    private InetAddress ip;
    private boolean statoAttivo=false;
    private Thread threadAscolto = null;
    private ServerThread threadRisposta = null;

    //eventi
    public static final String ASCOLTO_INIZIATO = "ascolto_iniziato";
    public static final String ASCOLTO_TERMINATO = "ascolto_terminato";
    private EventManagerCommandable eventManager = null;

    
     /**costruttore che prende due parametri, quindi se si usa questo non si può attivare il server, va settato il socket
     * 
     * @param nomeClient nome del server
     * @param t terminale che verra usato da questo server
     * @throws CommandableException
     */
    public Server(String nomeServer,EventManagerCommandable eventManager) throws CommandableException{
        if(eventManager == null) throw new CommandableException("Errore, l'eventManager è null!");
        this.eventManager = eventManager;
        this.setNome(nomeServer);
    }
    /**se uso quest costruttore, posso attivare il server
     * 
     * @param nomeClient nome del server
     * @param porta porta locale su cui aprire il socket
     * @param t riferimento terminale che verra usato da questo server
     * @throws CommandableException
     * @throws ErrorLogException
     */
    public Server(String nomeServer, String porta,EventManagerCommandable eventManager) throws CommandableException,ErrorLogException{
        this(nomeServer,eventManager);
        this.setPorta(porta);
        if(porta == null) throw new CommandableException("Errore, la porta è null!");
        try {
            this.socket = new DatagramSocket(this.porta);
        } catch (SocketException e) {
            throw new ErrorLogException(e.getMessage());
        }
    }
    public Server(String nomeServer, String ip,String porta,EventManagerCommandable eventManager) throws CommandableException,ErrorLogException{
        this(nomeServer,eventManager);
        if(porta == null || ip == null) throw new ErrorLogException("Errore, la porta o l'ip non sono stati specificati");
        this.setPorta(porta);
        this.setIp(ip);
        try {
            this.socket = new DatagramSocket(this.porta,this.ip);
        } catch (SocketException e) {
            throw new ErrorLogException(e.getMessage());
        }
    }


    /**implementa la logica di ricezione e attiva il thread di risposta
     * 
     */
    @Override
    public void run(){
        if(!this.isAttivo())return;
        byte[] bufferIN = new byte[Server.LunghezzaBuffer];
        while(this.isAttivo()){
            DatagramPacket pacchetto = new DatagramPacket(bufferIN, Server.LunghezzaBuffer);
            try {
                this.socket.receive(pacchetto);
                if(this.isAttivo()){
                    this.threadRisposta = null;
                    this.threadRisposta = new ServerThread(pacchetto, this.socket,this);
                    this.threadRisposta.start(); 
                }
            } catch (Exception e) {
                this.getEventManager().notify(e.getMessage().getBytes(),e.getMessage().getBytes().length,this);//! moddato
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
        this.getEventManager().notify(ASCOLTO_INIZIATO, this);//! notifico inizio ascolto
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
        this.getEventManager().notify(ASCOLTO_TERMINATO, this);//! notifico termine ascolto
    }



 
    /**imposta l'ip del socket remoto e controlla che sia corretto
     * 
     * @param ip ip di destinazione può essere "localhost" oppure un ip normale
     * @throws CommandableException
     */
    public void setIp(String ip) throws CommandableException{
        if(ip == null){ this.ip = null;return;}
        boolean temp = false;
        if(!ip.equalsIgnoreCase("localhost")){ 
            String[] valori = ip.split("[.]");
            if(valori.length != 4) temp = true;
            else{
                for (String string : valori){
                    if(!string.matches("^[0-9]{1,3}$"))temp = true;
                    if(Integer.valueOf(string) > 255)temp = true;
                }
            }
        }
        if(temp == true)throw new CommandableException("Errore, l'ip di destinazione inserito non è valido (deve avere formato '255.255.255.255'))");
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new CommandableException(e.getMessage());
        }
    }

    public String getIp(){ if(this.ip == null) return null; return this.ip.getHostAddress();}
    @Override
    public String toString() {
        return "Name: " + this.getNome() + "\t" + (this.socket == null?"Port: - ":("Port: "+this.getPorta())) + "\t" + (this.socket == null?"Ip: - ":("Ip: "+this.getIp()))+ "\tStatus: "+ (this.isAttivo() ? "attivo" : "disattivo");
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
     */
    public void setSocket(String porta) throws CommandableException, ErrorLogException{
        boolean wasActive = this.isAttivo();
        if(this.isAttivo())
            this.terminaAscolto();
        this.setPorta(porta);
        try {
            this.socket = new DatagramSocket(this.porta);
        } catch (SocketException e) {
            throw new ErrorLogException(e.getMessage());
        }
        if(wasActive)this.iniziaAscolto();
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(Server.class.isInstance(o)){
            return ((Server)o).getNome().equals(this.getNome());
        }
        return false;
    }

    public boolean socketIsSet(){
        if(this.socket == null) return false;
        return true;
    }

    public EventManagerCommandable getEventManager(){return this.eventManager;}

}

