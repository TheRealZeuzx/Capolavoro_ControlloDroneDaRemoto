public class CommandShowClient implements Command{
    private GestoreClientServer gestore;
    public CommandShowClient(GestoreClientServer gestore){
        this.gestore = gestore;
    }


    public boolean execute(){
        return this.gestore.stampaLista(true,false);
    }

}
