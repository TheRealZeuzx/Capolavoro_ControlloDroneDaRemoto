public class CommandShow implements Command{
    private GestoreClientServer gestore;
    private boolean client; 
    private boolean server;    
    public CommandShow(GestoreClientServer gestore,boolean client, boolean server){
        this.gestore = gestore;
    }
    public boolean execute(){
        if(!this.client && !this.server) return false;
        this.gestore.stampaLista(this.client, this.server);
        return true;
    }

}
