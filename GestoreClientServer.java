import java.util.ArrayList;
/**Gestore di client e server , è separato dal terminale in quanto ha metodi specifici per la gestione di client e server che la classe Terminal non deve gestire
 * 
 */
public class GestoreClientServer {

    private ArrayList<Server> listaServer;
    private ArrayList<Client> listaClient;
    private Terminal<GestoreClientServer> terminal;

    public GestoreClientServer(Error errorLog) throws CommandException{
        this.listaServer = new ArrayList<Server>(10); 
        this.listaClient = new ArrayList<Client>(10);
        this.terminal = new Terminal<GestoreClientServer>(this,GestoreClientServer.class);
    }


    public Server ricercaServer(String nome){
       
        return (Server)this.ricerca(nome, (ArrayList<SocketUDP>) ((ArrayList<?>) this.listaServer));
    }
    public Client ricercaClient(String nome){
        
        return (Client)this.ricerca(nome, (ArrayList<SocketUDP>) ((ArrayList<?>) this.listaClient));
    }
    
    public void creaServer(String nome,boolean attiva){

    }
    public void creaClient(String nome,boolean attiva){
        
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
            lista += "\n--- LISTA CLIENT ---";
            for (Client c : listaClient) {
            lista += c.toString() + "\n";
            }
        }
        if(server && this.listaServer.size() > 0){
            lista += "--- LISTA SERVER ---";
            for (Server s : listaServer) {
                lista += s.toString() + "\n";
            }
        }
        if(lista.isBlank()) return false;
        System.out.println(lista);
        return true;
    }


    public boolean remove(String nome,String tipo){

        return true;
    }

    /**ricerca di un socketUDP all interno di un vettore di SocketUDP (anche client o server)
     * 
     * @param nome nome del socket 
     * @param socket vettore di socketUDP
     * @return SocketUDP , restituisce il client o server se lo ha trovato altrimenti restituisce null
     */
    private SocketUDP ricerca(String nome,ArrayList<SocketUDP> socket){
        int i = 0;
        while(i<socket.size() && !socket.get(i).getNome().equals(nome)){
            i++;
        }
        return (i==socket.size() ? null : socket.get(i)); 
    }

    public void startTerminal(){
        this.terminal.main();
    }

}
