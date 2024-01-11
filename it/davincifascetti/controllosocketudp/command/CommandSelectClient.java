package it.davincifascetti.controllosocketudp.command;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;

public class CommandSelectClient extends CommandI<GestoreClientServer>{
    private String nome;
    public CommandSelectClient(GestoreClientServer gestore,String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
    }
    public void execute() throws CommandException {
        Client temp = this.getGestore().ricercaClient(this.nome);
        if(temp == null) throw new CommandException("il client '" + this.nome + "' non esiste");
        temp.startTerminal();
    }
    

}

