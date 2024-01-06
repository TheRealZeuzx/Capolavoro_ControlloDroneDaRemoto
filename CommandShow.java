public class CommandShow implements Command{
    private GestoreClientServer gestore;
    private boolean client; 
    private boolean server;    
    public CommandShow(GestoreClientServer gestore,boolean client, boolean server){
        this.gestore = gestore;
    }
    public boolean execute(){
        return this.gestore.stampaLista(this.client, this.server);
    }

}
