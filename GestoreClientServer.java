import java.util.ArrayList;

public class GestoreClientServer {

    private ArrayList<Server> listaServer;
    private ArrayList<Client> listaClient;

    public GestoreClientServer(){
        this.listaServer = new ArrayList<Server>(10); 
        this.listaClient = new ArrayList<Client>(10);
    }

    public Server ricercaServer(String nome){
        return null;
    }
    
    public void creaServer(String nome,boolean attiva){

    }
    public void creaClient(String nome,boolean attiva){
        
    }

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

    
    public void attiva(String nome,String tipo){

        
    }

}
