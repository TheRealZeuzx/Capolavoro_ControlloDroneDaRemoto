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

    public void stampaLista(boolean client,boolean server){
        String lista = (client == false && server == false)? "errore, non è stato specificato cosa stampare" : "";
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
        
        System.out.println(lista);
    }


    public boolean remove(String nome,String tipo){

        return true;
    }

    
    public void attiva(String nome,String tipo){

        
    }

}
