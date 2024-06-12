package it.davincifascetti.controllosocketudp.program;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**classe client si occupa di inviare msg al server e aspettare la risposta, se non arriva entro WAIT_TIME allora si termina la receive
 * @author Mattia Bonfiglio - Tommaso Mussaldi
 * @throws CommandableException errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 */
public class Client implements Commandable,Runnable{

	private static final int BUFFER_LENGHT = 1024;
	private static final int WAIT_TIME = 2000;
    private String nome;
    private String desc = null;

    private InetAddress ipDestinazioneDefault = null;
    private int porta = -1;
    private DatagramSocket socket;
    private byte[] bufferOUT = new byte[Client.BUFFER_LENGHT];

    //eventi
    public static final String MESSAGE_RECEIVED = "message_received";
    public static final String MESSAGE_SENT = "message_sent";
    public static final String SERVER_NO_RESPONSE = "server_no_response";
    public static final String UNKNOWN_EXCEPTION = "unknown_exception";
    private EventManagerCommandable eventManager = null;


    /**costruttore che prende due parametri, quindi se si usa questo non si possono invaire msg, va settato il socket remoto
     * 
     * @param nomeClient nome del client
     * @param t terminale che verra usato da questo client
     * @throws CommandableException
     */
    public Client(String nomeClient,EventManagerCommandable eventManager) throws CommandableException{
        if(eventManager == null) throw new CommandableException("Errore, l'eventManager è null!");
        this.eventManager = eventManager;
        this.setNome(nomeClient);
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new CommandableException(e.getMessage());
        }
    }
    /**se uso quest costruttore, posso invaire messaggi senza settare la socket
     * 
     * @param nomeClient nome del client
     * @param t riferimento terminale che verra usato da questo client
     * @param porta porta del socket remoto
     * @param ipDestinazioneDefault ip socket remoto
     * @throws CommandableException
     * @throws ErrorLogException
     */
    public Client(String nomeClient,String ipDestinazioneDefault,String porta,EventManagerCommandable eventManager) throws CommandableException, ErrorLogException{
        this(nomeClient,eventManager);
        if(porta == null || porta.isBlank() || ipDestinazioneDefault == null) throw new ErrorLogException("Errore, la porta o l'ip non sono stati specificati");
        try{
            this.setIpDestinazioneDefault(ipDestinazioneDefault);
        }catch(Exception e){
            throw new ErrorLogException(e.getMessage());
        }
        this.setPorta(porta);
    }


    /**Logica di ricezione della risposta del msg del server
     * setto il terminale su blocatto in modo da aspettare la risposta del Server prima che il terminale chieda un input
     * aspetta il WAIT_TIME , se in questo tempo non riceve niente allora è dato per perso
     */
    @Override
    public void run() {
        byte[] bufferIN = new byte[Client.BUFFER_LENGHT];
        //creo il pacchetto di ricezione
        DatagramPacket ricevuto = new DatagramPacket(bufferIN,bufferIN.length);
        //ricevo sul pacchetto di ricezione
        String msgRicevuto = null;
        try {
            
            this.socket.setSoTimeout(Client.WAIT_TIME);
            this.socket.receive(ricevuto);
            
            this.eventManager.notify(Client.MESSAGE_RECEIVED,this);
            this.eventManager.notify(ricevuto.getData(),ricevuto.getLength(),this);
        }catch(SocketTimeoutException e){
            this.eventManager.notify(Client.SERVER_NO_RESPONSE,this);
        } catch (Exception e) {
            this.eventManager.notify(Client.UNKNOWN_EXCEPTION,this);
        }
    }

    /**invia il messaggio al socket remoto, se non è impostato allora solleva un eccezione
     * 
     * @param msg messaggio da inviare
     * @throws CommandableException se la socket remota non è impostata o il msg è null
     */
    public void inviaMsg(String msg) throws CommandableException{
        if(!this.socketIsSet())throw new CommandableException("Errore, devi prima impostare il socket remoto");
        if(msg == null)throw new CommandableException("Errore, il messaggio inserito è null");
        this.bufferOUT = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(bufferOUT,bufferOUT.length,this.ipDestinazioneDefault,this.porta);
        try {
            this.socket.send(packet);
            this.eventManager.notify(Client.MESSAGE_SENT,this);
            this.ricevi();
        } catch (Exception e) {
            this.eventManager.notify(Client.UNKNOWN_EXCEPTION,this); //!se si volesse fare bene si dovrebbe mettere il multi-catch e una costante apposita per ogni eccezione
        }
    }
    /**invia il messaggio al socket remoto, se non è impostato allora solleva un eccezione
     * 
     * @param msg byte array
     * @throws CommandableException se la socket remota non è impostata o il msg è null
     */
    public void inviaMsg(byte[] msg) throws CommandableException{
        if(!this.socketIsSet())throw new CommandableException("Errore, devi prima impostare il socket remoto");
        if(msg == null)throw new CommandableException("Errore, il messaggio inserito è null");
        this.bufferOUT = msg;
        DatagramPacket packet = new DatagramPacket(bufferOUT,bufferOUT.length,this.ipDestinazioneDefault,this.porta);
        try {
            this.socket.send(packet);
            this.eventManager.notify(Client.MESSAGE_SENT,this);
            this.ricevi();
        } catch (Exception e) {
            this.eventManager.notify(Client.UNKNOWN_EXCEPTION,this);
        }
    }


    

    /**attiva il thread (del client attuale) quindi attiva la ricezione del msg da parte del server
     * vedere il metodo run di questa classe
     * 
     */
    public void ricevi(){
        Thread temp = new Thread(this);
        temp.start();
    }

    @Override
    public String toString() {
        return "Name: " + this.getNome() + (this.porta == -1 || this.ipDestinazioneDefault == null?"\tip: - \tporta: - ":("\tip: " + this.ipDestinazioneDefault.getHostAddress() + "\tporta: " + this.porta));
    }

    public String getNome(){return this.nome;}
    public int getPorta(){return this.porta;}
    /**
     * 
     * @return restituisce l'ip oppure null se non è impostato
     */
    public String getIp(){return (this.ipDestinazioneDefault != null ? this.ipDestinazioneDefault.getHostAddress() : null );}
    /**imposta il nome (deve avere almeno una lettera, può contenere numeri, lettere e _ min 1 carattere e max 18)
     * 
     * @param nome nuovo nome del client
     * @throws CommandableException
     */
    public void setNome(String nome) throws CommandableException{
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome != null && nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_0-9]{1,18}$"))
            this.nome = nome;
        else 
            throw new CommandableException("Errore, il nome '"+nome+"' inserito non è valido (deve contenere almeno una lettera, può contenere numeri da 0 a 9, lettere maiusc e minusc e '_')");
    }

    /**imposta l'ip del socket remoto e controlla che sia corretto
     * 
     * @param ip ip di destinazione può essere "localhost" oppure un ip normale
     * @throws CommandableException
     */
    public void setIpDestinazioneDefault(String ip) throws CommandableException{
        if(ip == null){ this.ipDestinazioneDefault = null;return;}
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
            this.ipDestinazioneDefault = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new CommandableException(e.getMessage());
        }
    }
    /**imposta la porta del socket remoto
     * 
     * @param port porta come Stringa perchè l'utente potrebbe scrivere una lettera e mandare in crash
     * @throws CommandableException
     */
    private void setPorta(String port)throws CommandableException{
        if(port == null){ this.porta = -1;return;}
        int p;
        try{
            p = Integer.valueOf(port);
        }catch(NumberFormatException e){
            throw new CommandableException("Errore, '" + port + "' non è un numero, specifica il numero della porta");
        }
        if(p < 0 || p > 65535)throw new CommandableException("Errore, la porta inserita non è valida (0-65535)");
        else this.porta = p;
    }

    /**invoca i metodi setPorta e setIpDestinazione 
     * 
     * @param ipDestinazioneDefault ip di destinazione
     * @param porta porta di destinazione
     * @throws CommandableException
     * @throws ErrorLogException
     */
    public void setSocket(String ipDestinazioneDefault,String porta) throws CommandableException, ErrorLogException{
        this.setPorta(porta);
        this.setIpDestinazioneDefault(ipDestinazioneDefault);
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(Client.class.isInstance(o)){
            return ((Client)o).getNome().equals(this.getNome());
        }
        return false;
    }


    public boolean socketIsSet(){
        if(this.porta == -1 || this.ipDestinazioneDefault == null)
            return false;
        return true;
    }
    @Override
    public EventManagerCommandable getEventManager() {
        return this.eventManager;
    }
    @Override
    public synchronized String getDesc() {
        return this.desc;
    }
    @Override
    public synchronized void setDesc(String desc) {
        this.desc = desc;
    }
    @Override
    public void destroy() {
        if(this.socketIsSet() && !this.socket.isClosed())this.socket.close();
        this.socket = null;
    }

}

