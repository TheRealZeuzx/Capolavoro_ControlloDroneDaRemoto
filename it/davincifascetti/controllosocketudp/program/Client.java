package it.davincifascetti.controllosocketudp.program;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**
 * @throws CommandableExceptio errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 */
public class Client implements Commandable,Runnable{

	private static final int LunghezzaBuffer = 1024;
    private String nome;
	private Terminal<Client> riferimentoTerminale;
    private InetAddress ipDestinazioneDefault = null;
    private int porta = -1;
    private DatagramSocket socket;
    private byte[] bufferOUT = new byte[Client.LunghezzaBuffer];


    public Client(String nomeClient,Terminal<Client> t) throws CommandableException{
        if(t == null)throw new CommandableException("il terminale inserito non è valido");
        this.riferimentoTerminale = t;
        this.setNome(nomeClient);
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new CommandableException(e.getMessage());
        }
    }

    public Client(String nomeClient,String ipDestinazioneDefault,String porta,Terminal<Client> t) throws CommandableException, ErrorLogException{
        this(nomeClient, t);
        try{
            this.setIpDestinazioneDefault(ipDestinazioneDefault);
        }catch(Exception e){
            throw new ErrorLogException(e.getMessage());
        }
        this.setPorta(porta);
    }


    /**Logica di ricezione della risposta da parte del server
     * 
     */
    @Override
    public void run() {
        byte[] bufferIN = new byte[Client.LunghezzaBuffer];
        //creo il pacchetto di ricezione
        DatagramPacket ricevuto = new DatagramPacket(bufferIN,bufferIN.length);
        //ricevo sul pacchetto di ricezione
        String msgRicevuto = null;
        try {
            
            this.socket.setSoTimeout(2000);
            this.socket.receive(ricevuto);
            msgRicevuto = new String(ricevuto.getData());
            msgRicevuto = msgRicevuto.substring(0, ricevuto.getLength());
            System.out.println("Server response: " + msgRicevuto);
        }catch(SocketTimeoutException e){
            this.riferimentoTerminale.errorLog("Il server non ha dato nessuna risposta", true);
        } catch (Exception e) {
            this.riferimentoTerminale.errorLog(e.getMessage(), false);
        }
        // //se il ricevuto ha lung 0 allora non ho ricevuto niente
        // if(ricevuto.getLength() == 0)this.riferimentoTerminale.errorLog("Il server non ha dato nessuna risposta", true);
        // else{
        //     //creo stringa ricevuta dall buffer di dati ricevuti
            
            
        // }
    }

    public void inviaMsg(String msg) throws CommandableException{
        if(this.porta == -1 || this.ipDestinazioneDefault == null)throw new CommandableException("Errore, devi prima impostare il socket remoto");
        if(msg == null)throw new CommandableException("Errore, il messaggio inserito è null");
        this.bufferOUT = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(bufferOUT,bufferOUT.length,this.ipDestinazioneDefault,this.porta);
        try {
            this.socket.send(packet);
            this.ricevi();
        } catch (Exception e) {
            this.riferimentoTerminale.errorLog(e.getMessage(), true);
        }
    }

    public void ricevi(){
        Thread temp = new Thread(this);
        temp.start();
        // try {
        //     temp.join();
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

    @Override
    public String toString() {
        
        return "Name: " + this.getNome() + (this.porta == -1 || this.ipDestinazioneDefault == null?"\tip: - \tporta: - ":("\tip: " + this.ipDestinazioneDefault.getHostAddress() + "\tporta: " + this.porta));
    }

    @Override
    public void startTerminal() throws CommandException {
        this.riferimentoTerminale.main(this);
    }

    public String getNome(){return this.nome;}
    public int getPorta(){return this.porta;}
    public String getIp(){return (this.ipDestinazioneDefault != null ? this.ipDestinazioneDefault.getHostAddress() : null );}
    public void setNome(String nome) throws CommandableException{
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_0-9]{1,18}$"))
            this.nome = nome;
        else 
            throw new CommandableException("Errore, il nome '"+nome+"' inserito non è valido (deve contenere almeno una lettera, può contenere numeri da 0 a 9, lettere maiusc e minusc e '_')");
    }

    public void setIpDestinazioneDefault(String ip) throws CommandableException{
        boolean temp = false;
        if(!ip.equalsIgnoreCase("localhost")){ 
            String[] valori = ip.split(".");
            if(valori.length <= 2 || valori.length > 3) temp = true;
            else{
                for (String string : valori){
                    if(!string.matches("^[1-9]{1,3}$"))temp = true;
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

    public void setSocket(String ipDestinazioneDefault,String porta) throws CommandableException, ErrorLogException{
        this.setPorta(porta);
        this.setIpDestinazioneDefault(ipDestinazioneDefault);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Client){
            return ((Client)o).getNome().equals(this.getNome());
        }
        return false;
    }

}

