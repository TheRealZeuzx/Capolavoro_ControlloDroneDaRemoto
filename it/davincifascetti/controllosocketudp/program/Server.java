package it.davincifascetti.controllosocketudp.program;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**
 * @throws CommandableExceptio errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 */
public class Server implements Runnable,Commandable{

    //TODO creare i seguenti metodi per eseguire le op di :
    //TODO salva su file msg client
    
    public static final int LunghezzaBuffer = 1024;
    private DatagramSocket socket;
    private String nome;
    private int porta;
    private boolean statoAttivo=false;
    private Thread threadAscolto = null;
    private Terminal<Server> riferimentoTerminale;
    private ArrayList<String> StoriaMsg;
    private CommandHistory storiaComandiRisposta;

    public Server(String nomeServer,Terminal<Server> t) throws CommandableException{
        if(t == null)throw new CommandableException("il terminale inserito non è valido");
        this.riferimentoTerminale = t;
        this.setNome(nomeServer);
        this.StoriaMsg = new ArrayList<String>();
        this.storiaComandiRisposta = new CommandHistory();
    }

    public Server(String nomeServer, String porta,Terminal<Server> t) throws CommandableException,ErrorLogException{
        this(nomeServer,t);
        this.setPorta(porta);
        try {
            this.socket = new DatagramSocket(this.porta);
        } catch (SocketException e) {
            throw new ErrorLogException(e.getMessage());
        }
    }
    
    @Override
    public void run(){
        if(!this.isAttivo())return; //se provo a creare un thread di Server e avviarlo senza usare il metodo iniziaAscolto allora non eseguo
        byte[] bufferIN = new byte[Server.LunghezzaBuffer];
        //TODO se è attivo && vivo si mette 
        while(this.isAttivo()){
            DatagramPacket pacchetto = new DatagramPacket(bufferIN, Server.LunghezzaBuffer);
            try {
                this.socket.receive(pacchetto);
                if(this.isAttivo()){
                    Thread threadRisposta = null;
                    threadRisposta = new Thread(new ServerThread(pacchetto, this.socket,this.StoriaMsg,this.riferimentoTerminale, this.storiaComandiRisposta,this.getNome()));
                    threadRisposta.start(); 
                }
            } catch (Exception e) {
                this.riferimentoTerminale.errorLog(e.getMessage(),false);
            }
        } 
        if(!this.socket.isClosed())this.socket.close();
    }

    public void iniziaAscolto()throws CommandableException{
        if(this.socket == null) throw new CommandableException("Errore, la socket è null non può essere avviato, imposta una porta prima");
        this.statoAttivo = true;
        if(this.threadAscolto == null)this.threadAscolto = new Thread(this);
        if(!this.threadAscolto.isAlive())this.threadAscolto.start(); 
    }

    public void terminaAscolto(){
        this.statoAttivo = false;
        if(this.socket != null){
            if(!this.socket.isClosed())this.socket.close();
        }
        if(this.threadAscolto != null ) this.threadAscolto.interrupt();
        this.threadAscolto = null;
    }

    @Override
    public String toString() {
        return "Name: " + this.getNome() + "\t" + (this.socket == null?"Port: - ":("Port: "+this.getPorta()))  +"\tStatus: "+ (this.isAttivo() ? "attivo" : "disattivo");
    }

    public String stampaStoriaMsg(){
        String temp = "";
        for (String string : StoriaMsg) {
            temp +=string+"\n";
        }
        return temp;
    }

    @Override
    public void startTerminal() throws CommandException {
        this.riferimentoTerminale.main(this);
    }

    public boolean isAttivo(){return this.statoAttivo;}

    public String getNome(){return this.nome;}

    public void setNome(String nome) throws CommandableException{
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_0-9]{1,18}$"))
            this.nome = nome;
        else 
            throw new CommandableException("Errore, il nome '"+nome+"' inserito non è valido (deve contenere almeno una lettera, può contenere numeri da 0 a 9, lettere maiusc e minusc e '_')");
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
    public int getPorta(){return this.porta;}
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
        if(o instanceof Server){
            return ((Server)o).getNome().equals(this.getNome());
        }
        return false;
    }

}
