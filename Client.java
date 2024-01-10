
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
/**
 * @throws CommandableExceptio errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 */
public class Client implements Runnable,Commandable{

	private static final int LunghezzaBuffer = 1024;
    private String nome;
    private boolean stato;
	private Terminal<Client> riferimentoTerminale;
    private String ip;
    private int porta;
    private DatagramSocket socket;

    public Client(String nomeClient,Terminal<Client> t) throws CommandableException{
        this.setNome(nomeClient);
		this.riferimentoTerminale = t;
    }
    public Client(String nomeClient,String ip,int porta,Terminal<Client> t) throws CommandableException{
        this(nomeClient, t);
        this.setIp(ip);
        this.setPorta(porta);
    }

    @Override
    public void run() {
    }

    public void main(){
        byte[] bufferIN = new byte[Client.LunghezzaBuffer];
        byte[] bufferOUT = new byte[Client.LunghezzaBuffer];  
        
    }

    @Override
    public String toString() {
        //TODO stampa nome , socket remoto (ip remoto e porta) , stato
        return this.getNome() +"\t"+  (this.isAttivo() ? "attivo" : "disattivo");
    }

    @Override
    public void startTerminal() throws CommandException {
        this.riferimentoTerminale.main(this);
    }

    public boolean isAttivo(){return this.stato;}

    public void setStato(boolean stato){this.stato = stato;}

    public String getNome(){return this.nome;}

    public void setNome(String nome) throws CommandableException{
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_]{2,18}$"))
            this.nome = nome;
        else 
            throw new CommandableException("Errore, il nome '"+nome+"' inserito non è valido (solo lettere min, maiusc e '_')");
    }
    public void setIp(String ip) throws CommandableException{
        boolean temp = false;
        if(ip.equalsIgnoreCase("localhost")) this.ip = ip;
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
        if(temp == true)throw new CommandableException("Errore, l'ip inserito non è valido (deve avere formato '255.255.255.255'))");
        this.ip = ip;
    }
    public void setPorta(int p)throws CommandableException{
        if(p < 0 || p > 65535)throw new CommandableException("Errore, la porta inserita non è valida (0-65535)");
        else this.porta = p;
    }
    
    //TODO
    public void modificaSocket(int porta) throws CommandableException, ErrorLogException{
        // if(this.isAttivo())
        //     this.terminaAscolto();
        // this.setPorta(porta);
        // try {
        //     this.socket = new DatagramSocket(this.porta);
        // } catch (SocketException e) {
        //     throw new ErrorLogException(e.getMessage());
        // }
        // this.iniziaAscolto();
    }

}

