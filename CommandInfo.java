public class CommandInfo implements Command{
    private GestoreClientServer gestore;
    private String nome;  
    private String tipo;  
    public CommandInfo(GestoreClientServer gestore,String[] params) throws CommandException{
        this.gestore = gestore;
        this.nome = nome;
        this.tipo = tipo;
    }
    public boolean execute() {
        
        return true;
    }
    
}
