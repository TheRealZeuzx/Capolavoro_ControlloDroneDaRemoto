public class CommandShowServer implements Command{
    private GestoreClientServer gestore;
    public CommandShowServer(GestoreClientServer gestore) throws CommandException{
        this.gestore = gestore;
    }

    public boolean execute(){
        return this.gestore.stampaLista(false,true);
    }

}

