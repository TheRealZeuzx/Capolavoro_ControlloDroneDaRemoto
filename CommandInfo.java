/**Questo comando permette di ricercare tramite il gestoreClientServer un client o server specifico e di mostrarne il toString
 * 
 */
public class CommandInfo extends CommandI<GestoreClientServer>{
    private String nome;  
    private boolean client;  
    public CommandInfo(GestoreClientServer gestore,boolean client,String nome ) throws CommandException{
        super(gestore);
        this.nome=nome;
        this.client = client;
    }
    public void execute() throws CommandException{
        if(client){
            if(this.getGestore().isEmpty(true)) throw new CommandException("La lista di client è vuota");
            this.getGestore().ricercaClient(nome).toString();
        }else{
            if(this.getGestore().isEmpty(false)) throw new CommandException("La lista di server è vuota");
            this.getGestore().ricercaServer(nome).toString();
        }
        
        
    }
    
}
