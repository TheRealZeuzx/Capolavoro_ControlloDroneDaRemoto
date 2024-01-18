package it.davincifascetti.controllosocketudp.program;
import java.util.ArrayList;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**Gestore di client e server , è separato dal terminale in quanto ha metodi specifici per la gestione di client e server
 * contiene al suo interno il proprio terminale che esegue i propri command
 * 
 * @throws CommandableExceptio errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 *
 */
public class GestoreClientServer implements Commandable{

    private ArrayList<Server> listaServer;
    private ArrayList<Client> listaClient;
    private Terminal<GestoreClientServer> terminal;
    private Terminal<Server> terminalS;
    private Terminal<Client> terminalC;
    public GestoreClientServer(ErrorLog errorLog) throws CommandException{

        this.listaServer = new ArrayList<Server>(10);
        this.listaClient = new ArrayList<Client>(10);
        this.terminal = new Terminal<GestoreClientServer>(errorLog);
        this.terminalC = new Terminal<Client>(errorLog);
        this.terminalS = new Terminal<Server>(errorLog);
        
    }


    public Terminal<Client> getTerminalClient(){
        return this.terminalC;
    }
    public Terminal<Server> getTerminalServer(){
        return this.terminalS;
    }


    public Server ricercaServer(String nome){
        int i = 0;
        while(i<this.listaServer.size() && !this.listaServer.get(i).getNome().equals(nome)){
            i++;
        }
        return (i==this.listaServer.size() ? null : this.listaServer.get(i)); 

    }
    public Client ricercaClient(String nome){
        int i = 0;
        while(i<this.listaClient.size() && !this.listaClient.get(i).getNome().equals(nome)){
            i++;
        }
        return (i==this.listaClient.size() ? null : this.listaClient.get(i)); 

    }
    
    public void newClient(Terminal<Client> terminale, String nome) throws CommandableException{
        if(terminale == null) throw new CommandableException("Errore, il terminale specificato è null");
        if(ricercaClient(nome) != null) throw new CommandableException("il client '" + nome + "' è già esistente");
        Client c = new Client(nome,terminale);
        this.listaClient.add(c);
        
    }
    public void newClient(Terminal<Client> terminale, String nome,String ip,String porta) throws CommandableException, ErrorLogException{
        if(terminale == null) throw new CommandableException("Errore, il terminale specificato è null");
        if(ip == null || ip.equals("")) throw new CommandableException("Errore, l'ip non è stato specificato");
        if(ricercaClient(nome) != null) throw new CommandableException("il client '" + nome + "' è già esistente");
        Client c = new Client(nome,ip,porta,terminale);
        this.listaClient.add(c);
    }

    public void newServer(Terminal<Server> terminale, String nome) throws CommandableException{
        if(ricercaServer(nome) != null) throw new CommandableException("il server '" + nome + "' è già esistente");
        Server s = new Server(nome,terminale);
        this.listaServer.add(s);
        
    }
    public void newServer(Terminal<Server> terminale, String nome,String porta) throws CommandableException, ErrorLogException{
        if(terminale == null) throw new CommandableException("Errore, il terminale specificato è null");
        if(ricercaServer(nome) != null) throw new CommandableException("il server '" + nome + "' è già esistente");
        Server s = new Server(nome,porta,terminale);
        this.listaServer.add(s);
        s.iniziaAscolto();
        
    }
    
    public void addServer(Server s)throws CommandableException{
        if(s == null) throw new CommandableException("Errore, il server che hai provato ad inserire è null");
        this.listaServer.add(s);
    }
    public void addClient(Client c)throws CommandableException{
        if(c == null) throw new CommandableException("Errore, il server che hai provato ad inserire è null");
        this.listaClient.add(c);
    }
    public boolean isEmpty(boolean client){
        if(client) return this.listaClient.isEmpty();
        return this.listaServer.isEmpty();
    }
    /**permette di stampare la lista di client e server presenti in base ai parametri passati
     * 
     * @param client valore booleano che mi permette di capire se stampare la lista di client o meno
     * @param server valore booleano che mi permette di capire se stampare la lista di server o meno
     * @return true se la stampa è avvenuta con successo altrimenti false
     */
    public boolean stampaLista(boolean client,boolean server){
        //messaggi in base alla presenza di elementi in listaServer e listaClient
        String lista = this.listaClient.size() == 0 && client ? "non è presente nessun client" : "";
        lista = this.listaServer.size() == 0 && server ? "non è presente nessun server" : lista;
        lista = this.listaClient.size() == 0 && this.listaServer.size() == 0 ? "non è presente nessun client o server" : lista;
        if(client == false && server == false) return false;
        if(client && this.listaClient.size() > 0){
            lista += "\n--- LISTA CLIENT ---\n";
            for (Client c : listaClient) {
            lista += c.toString() + "\n";
            }
        }
        if(server && this.listaServer.size() > 0){
            lista += "\n--- LISTA SERVER ---\n";
            for (Server s : listaServer) {
                lista += s.toString() + "\n";
            }
        }
        if(lista.isBlank()) return false;
        System.out.println(lista);
        return true;
    }


    public void removeClient(Client c)throws CommandableException {
        if(!this.listaClient.remove(c)) throw new CommandableException("il client '" + c.getNome() + "' non esiste");
    }
    public void removeServer(Server s)throws CommandableException {
        if(!this.listaServer.remove(s)) throw new CommandableException("il server '" + s.getNome() + "' non esiste");
    }

    public void startTerminal() throws CommandException{
        this.terminal.main(this);
    }


    

}
