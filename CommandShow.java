public class CommandShow implements Command{
    private GestoreClientServer gestore;
    private String scelta;  
    public CommandShow(GestoreClientServer gestore,String scelta){
        this.gestore = gestore;
        this.scelta = scelta;
    }

    //? si può fare o è meglio dividere in 2 comandi showclient e show server e poi utilizzare un compositeCommand oppure showall command?
    public boolean execute(){
        
        boolean client = false;
        boolean server = false;
        switch (this.scelta) {
            case "a":
            case "all":
                server = true;client = true;
                break;
            case "c":
            case "client":
                server = false;client = true;
                break;
            case "s":
            case "server":
                server = true;client = false;
                break;
            default:
                break;
        }
        return this.gestore.stampaLista(client,server);
    }

}
