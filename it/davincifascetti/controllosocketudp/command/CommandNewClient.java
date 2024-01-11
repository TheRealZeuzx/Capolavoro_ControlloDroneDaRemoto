package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Terminal;

public class CommandNewClient extends CommandI<GestoreClientServer> implements UndoableCommand{

    private String nome;
    private Terminal<Client> terminale;
    private int porta = -1;
    private String ip = "";
    public CommandNewClient(GestoreClientServer gestore,Terminal<Client> terminale, String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
        this.terminale = terminale;
    }
    public CommandNewClient(GestoreClientServer gestore,Terminal<Client> terminale, String nome,String ip,int porta) throws CommandException {
        this(gestore, terminale, nome);
        this.ip = ip;
        this.porta = porta;
    }

    @Override
    public void execute() throws CommandException {
        try{
        if(ip.equals("") || porta == -1)
            this.getGestore().newClient(terminale,nome);
        else
            this.getGestore().newClient(terminale,nome,ip,porta);
        }catch(CommandableException e){
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean undo() throws CommandException {
        new CommandDeleteClient(getGestore(), nome).execute();
        return true;
    }
    
}
