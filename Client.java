
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client implements Runnable,Commandable{

	private static final int LunghezzaBuffer = 1024;
    private String nome;
    private boolean stato;
	private Terminal<Client> riferimentoTerminale;

    public Client(DatagramSocket socket,String nomeClient,Terminal<Client> t) throws Exception{
        if(!this.setNome(nomeClient)) throw new Exception("il nome inserito non è valido");
		this.riferimentoTerminale = t;
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
        
        return this.getNome() +"\t"+ (this.getStato() ? "attivo" : "disattivo");
    }

    @Override
    public void startTerminal() {
        this.riferimentoTerminale.main(this);
    }

    public boolean getStato(){return this.stato;}

    public void setStato(boolean stato){this.stato = stato;}

    public String getNome(){return this.nome;}

    public boolean setNome(String nome){
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_]{2,18}$")){
            this.nome = nome;
            return true;
        }
        return false;
    }
}

