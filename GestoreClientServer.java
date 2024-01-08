import java.util.ArrayList;
/**Gestore di client e server , è separato dal terminale in quanto ha metodi specifici per la gestione di client e server
 * contiene al suo interno il proprio terminale che esegue i propri command
 * 
 */
public class GestoreClientServer implements Commandable{

    private ArrayList<Server> listaServer;
    private ArrayList<Client> listaClient;
    private Terminal<GestoreClientServer> terminal;

    public GestoreClientServer(Error errorLog) throws CommandException{
        this.listaServer = new ArrayList<Server>(10); 
        this.listaClient = new ArrayList<Client>(10);
        this.terminal = new Terminal<GestoreClientServer>(this,GestoreClientServer.class);
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
    
    public void creaServer(String nome,boolean attiva){

    }
    public void creaClient(String nome,boolean attiva){
        
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

    public void startTerminal(){
        this.terminal.main();
    }

}
