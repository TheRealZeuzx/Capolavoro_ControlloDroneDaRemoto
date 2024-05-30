package it.davincifascetti.controllosocketudp.program;
import java.util.ArrayList;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**Gestore di client e server , è separato dal terminale in quanto ha metodi specifici per la gestione di client e server
 * contiene al suo interno il proprio terminale che esegue i propri command e il terminale di client e server che sono unici per tutti i client e server
 * ha metodi separati per ricerca aggiunta e eliminazione dalle liste client e server
 * @throws CommandableExceptio errori stampati sul terminale
 * @throws ErrorLogException errori stampati sul file
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
  *  @version 1.0
 */

public class GestoreClientServer implements Commandable{
    private ArrayList<Server> listaServer;
    private ArrayList<Client> listaClient;
    //eventi
    public static final String CLIENT_RIMOSSO = "client_rimosso";
    public static final String SERVER_RIMOSSO = "server_rimosso";
    public static final String CLIENT_AGGIUNTO = "client_aggiunto";
    public static final String SERVER_AGGIUNTO = "server_aggiunto";
    private EventManagerCommandable eventManager= new EventManagerCommandable(
        CLIENT_RIMOSSO,
        SERVER_RIMOSSO,
        CLIENT_AGGIUNTO,
        SERVER_AGGIUNTO
    );
    //!problema: l'oggetto è comune a tutti i client/server
    //TODO probabilmente far diventare EventManagerCommandable una interfaccia, creare per client e per server una Map di client, Map<String, ArrayList<EventListenerCommandable>>
    private EventManagerCommandable eventManagerClient = new EventManagerCommandable(
        Client.MESSAGGIO_RICEVUTO,
        Client.MESSAGGIO_INVIATO,
        Client.SERVER_NO_RESPONSE,
        Client.UNKNOWN_EXCEPTION
    );
    private EventManagerCommandable eventManagerServer= new EventManagerCommandable(Server.ASCOLTO_INIZIATO,Server.ASCOLTO_TERMINATO);
    

    /**devo passargli il Errorlog che sarà unico tra tutte le classi che devono utilizzarlo
     * 
     * @param errorLog oggetto error log
     * @throws CommandException
     */
    public GestoreClientServer() throws CommandException{
        this.listaServer = new ArrayList<Server>(10);
        this.listaClient = new ArrayList<Client>(10);
    }


    /**ricerca nella lista di server
     * 
     * @param nome nome del server in cui cercare
     * @return null se non lo  trova altrimenti il Server
     */
    public Server ricercaServer(String nome){
        int i = 0;
        while(i<this.listaServer.size() && !this.listaServer.get(i).getNome().equals(nome)){
            i++;
        }
        return (i==this.listaServer.size() ? null : this.listaServer.get(i)); 

    }
     /**ricerca nella lista di client
     * 
     * @param nome nome del client in cui cercare
     * @return null se non lo  trova altrimenti il client
     */
    public Client ricercaClient(String nome){
        int i = 0;
        while(i<this.listaClient.size() && !this.listaClient.get(i).getNome().equals(nome)){
            i++;
        }
        return (i==this.listaClient.size() ? null : this.listaClient.get(i)); 

    }
    
    /**permette di instanziare un nuovo client e inserirlo nella lista di client, utilizza il primo costruttore di client
     * 
     * @param terminale terminale da di client da passare al costruttore di client
     * @param nome nome del client
     * @throws CommandableException
     */
    public void newClient(String nome) throws CommandableException{
        if(ricercaClient(nome) != null) throw new CommandableException("il client '" + nome + "' è già esistente");
        Client c = new Client(nome);
        this.listaClient.add(c);
        this.eventManager.notify(GestoreClientServer.CLIENT_AGGIUNTO, c);
        
    }
    /**permette di instanziare un nuovo client e inserirlo nella lista di client, utilizza il secondo costruttore di client
     * 
     * @param terminale terminale da di client da passare al costruttore di client
     * @param nome nome del client
     * @param ip ip del server remoto
     * @param porta porta del server remoto
     * @throws CommandableException
     * @throws ErrorLogException
     */
    public void newClient(String nome,String ip,String porta) throws CommandableException, ErrorLogException{
        if(ip == null || ip.equals("")) throw new CommandableException("Errore, l'ip non è stato specificato");
        if(ricercaClient(nome) != null) throw new CommandableException("il client '" + nome + "' è già esistente");
        Client c = new Client(nome,ip,porta);
        this.listaClient.add(c);
        this.eventManager.notify(GestoreClientServer.CLIENT_AGGIUNTO, c);
    }

    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il primo costruttore di server
     * 
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @throws CommandableException
     */
    public void newServer(String nome) throws CommandableException{
        if(ricercaServer(nome) != null) throw new CommandableException("il server '" + nome + "' è già esistente");
        Server s = new Server(nome);
        this.listaServer.add(s);
        this.eventManager.notify(GestoreClientServer.SERVER_AGGIUNTO, s);
        
    }
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il secondo costruttore di server
     * 
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @param porta porta locale del server
     * @throws CommandableException
     * @throws ErrorLogException
     */
    public void newServer(String nome,String porta) throws CommandableException, ErrorLogException{
        if(ricercaServer(nome) != null) throw new CommandableException("il server '" + nome + "' è già esistente");
        Server s = new Server(nome,porta);
        this.listaServer.add(s);
        s.iniziaAscolto();
        this.eventManager.notify(GestoreClientServer.SERVER_AGGIUNTO, s);
        
    }
    /**permette di instanziare un nuovo server e inserirlo nella lista di server, utilizza il secondo costruttore di server
     * 
     * @param terminale terminale da di server da passare al costruttore di server
     * @param nome nome del server
     * @param porta porta locale del server
     * @param ip porta locale del server
     * @throws CommandableException
     * @throws ErrorLogException
     */
    public void newServer(String nome,String ip,String porta) throws CommandableException, ErrorLogException{
        if(ricercaServer(nome) != null) throw new CommandableException("il server '" + nome + "' è già esistente");
        Server s = new Server(nome,ip,porta);
        this.listaServer.add(s);
        s.iniziaAscolto();
        this.eventManager.notify(GestoreClientServer.SERVER_AGGIUNTO, s);
        
    }
    
    /**permette di aggiungere un server alla lista di server (gia instanziato)
     * 
     * @param s server da aggiungere
     * @throws CommandableException
     */
    public void addServer(Server s)throws CommandableException{
        if(s == null) throw new CommandableException("Errore, il server che hai provato ad inserire è null");
        this.listaServer.add(s);
        this.eventManager.notify(GestoreClientServer.SERVER_AGGIUNTO, s);
    }
    /**permette di aggiungere un client alla lista di client (gia instanziato)
     * 
     * @param c client da aggiungere
     * @throws CommandableException
     */
    public void addClient(Client c)throws CommandableException{
        if(c == null) throw new CommandableException("Errore, il server che hai provato ad inserire è null");
        this.listaClient.add(c);
        this.eventManager.notify(GestoreClientServer.CLIENT_AGGIUNTO, c);
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


    /**permette di rimuovere un client dalla lista di client
     * 
     * @param c client da rimuovere
     * @throws CommandableException
     */
    public void removeClient(Client c)throws CommandableException {
        if(c == null) throw new CommandableException("il client è null!");
        if(!this.listaClient.remove(c)) throw new CommandableException("il client '" + c.getNome() + "' non esiste");
        this.eventManager.notify(CLIENT_RIMOSSO, c);
    }
    /**permette di rimuovere un server dalla lista server
     * 
     * @param s server da rimuvoere
     * @throws CommandableException
     */
    public void removeServer(Server s)throws CommandableException {
        if(s == null) throw new CommandableException("il server è null!");
        if(!this.listaServer.remove(s)) throw new CommandableException("il server '" + s.getNome() + "' non esiste");
        this.eventManager.notify(SERVER_RIMOSSO, s);

    }

    public EventManagerCommandable getEventManager(){return this.eventManager;}
    public EventManagerCommandable getEventManagerClient(){return this.eventManagerClient;}
    public EventManagerCommandable getEventManagerServer(){return this.eventManagerServer;}

}
