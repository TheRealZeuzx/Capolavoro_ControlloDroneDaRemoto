package it.davincifascetti.controllosocketudp.program;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**
 * @throws CommandableExceptio errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 */
public class Client implements Commandable{

	private static final int LunghezzaBuffer = 1024;
    private String nome;
    private boolean stato;
	private Terminal<Client> riferimentoTerminale;
    private InetAddress ipDestinazioneDefault;
    private int porta;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private boolean statoAttivo=false;
    private byte[] bufferOUT = new byte[Client.LunghezzaBuffer];


    public Client(String nomeClient,Terminal<Client> t) throws CommandableException{
        this.setNome(nomeClient);
		this.riferimentoTerminale = t;
    }

    public Client(String nomeClient,String ipDestinazioneDefault,int porta,Terminal<Client> t) throws CommandableException{
        this(nomeClient, t);
        try{
            this.setIpDestinazioneDefault(ipDestinazioneDefault);
        }catch(Exception e){
            this.riferimentoTerminale.errorLog(e.getMessage());
        }
        this.setPorta(porta);
    }

    public boolean inviaMsg(String msg){
        if(this.socket == null || this.packet == null)
            return false;
		bufferOUT = msg.getBytes();
		try {
			this.socket.send(this.packet);
            return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
            return false;
		}
    }

    @Override
    public String toString() {
        //TODO stampa nome , socket remoto (ipDestinazioneDefault remoto e porta) , stato
        return this.getNome() +"\t"+  (this.isStatoAttivo() ? "statoAttivo" : "isStatoAttivo");
    }

    @Override
    public void startTerminal() throws CommandException {
        this.riferimentoTerminale.main(this);
    }

    public boolean isStatoAttivo(){return this.stato;}

    public void setStato(boolean stato){this.stato = stato;}

    public String getNome(){return this.nome;}

    public void setNome(String nome) throws CommandableException{
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_]{2,18}$"))
            this.nome = nome;
        else 
            throw new CommandableException("Errore, il nome '"+nome+"' inserito non è valido (solo lettere min, maiusc e '_')");
    }

    public void setIpDestinazioneDefault(String ip) throws CommandableException, UnknownHostException{
        boolean temp = false;
        if(ip.equalsIgnoreCase("localhost")) this.ipDestinazioneDefault = InetAddress.getByName(ip);
        else{ 
            String[] valori = ip.split(".");
            if(valori.length <= 2 || valori.length > 3) temp = true;
            else{
                for (String string : valori){
                    if(!string.matches("^[1-9]{1,3}$"))temp = true;
                    if(Integer.valueOf(string) > 255)temp = true;
                }
            }
        }
        if(temp == true)throw new CommandableException("Errore, l'ipDestinazioneDefault inserito non è valido (deve avere formato '255.255.255.255'))");
        this.ipDestinazioneDefault = InetAddress.getByName(ip);
    }
    public void setPorta(int p)throws CommandableException{
        if(p < 0 || p > 65535)throw new CommandableException("Errore, la porta inserita non è valida (0-65535)");
        else this.porta = p;
    }
    
    public void iniziaAscolto()throws CommandableException{
        if(this.socket == null) throw new CommandableException("Errore, la socket è null non può essere avviato, imposta una porta prima");
        this.statoAttivo = true;
    }

    public void terminaAscolto(){
        this.statoAttivo = false;
        if(this.socket != null){
            if(!this.socket.isClosed())this.socket.close();
        }
    }

    public void setSocket(int porta,String ipDestinazioneDefault) throws CommandableException, ErrorLogException{
        if(this.isStatoAttivo())
            this.terminaAscolto();
        this.setPorta(porta);
        try {
            this.socket = new DatagramSocket(this.porta, this.ipDestinazioneDefault);
        } catch (SocketException e) {
            throw new ErrorLogException(e.getMessage());
        }
        this.iniziaAscolto();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Client){
            return ((Client)o).getNome().equals(this.getNome());
        }
        return false;
    }

}

