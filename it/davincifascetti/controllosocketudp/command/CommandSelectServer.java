package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Server;

public class CommandSelectServer extends CommandI<GestoreClientServer>{
    private String nome;
    public CommandSelectServer(GestoreClientServer gestore,String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
    }
    public void execute() throws CommandException {
        Server temp = this.getGestore().ricercaServer(this.nome);
        if(temp == null) throw new CommandException("il server '" + this.nome + "' non esiste");
        temp.startTerminal();
    }
    

}
