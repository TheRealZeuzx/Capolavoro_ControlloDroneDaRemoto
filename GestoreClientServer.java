public class GestoreClientServer {

    private Server []listaServer;
    private Client []listaClient;

    public GestoreClientServer(){
        this.listaServer = new Server[10]; 
        this.listaClient = new Client[10];
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
        String lista = this.listaClient.length == 0 && client ? "non è presente nessun client" : "";
        lista = this.listaServer.length == 0 && server ? "non è presente nessun server" : lista;
        lista = this.listaClient.length == 0 && this.listaServer.length == 0 ? "non è presente nessun client o server" : lista;
        if(client == false && server == false) return false;
        if(client){
            lista += "\n--- LISTA CLIENT ---";
            for (Client c : listaClient) {
            lista += c.toString() + "\n";
            }
        }
        if(server){
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
