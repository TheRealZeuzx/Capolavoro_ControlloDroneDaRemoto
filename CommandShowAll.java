public class CommandShowAll implements Command{
    private GestoreClientServer gestore; 
    public CommandShowAll(GestoreClientServer gestore){
        this.gestore = gestore;
    }


    public boolean execute(){
        return this.gestore.stampaLista(true,true);
    }

}
