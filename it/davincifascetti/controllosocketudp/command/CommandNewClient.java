package it.davincifascetti.controllosocketudp.command;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;
import it.davincifascetti.controllosocketudp.program.GestoreClientServer;
import it.davincifascetti.controllosocketudp.program.Terminal;

public class CommandNewClient extends CommandI<GestoreClientServer> implements UndoableCommand{

    private String nome;
    private Terminal<Client> terminale;
    private String porta = null;
    private String ip = "";
    public CommandNewClient(GestoreClientServer gestore,Terminal<Client> terminale, String nome) throws CommandException {
        super(gestore);
        this.nome = nome;
        this.terminale = terminale;
    }
    public CommandNewClient(GestoreClientServer gestore,Terminal<Client> terminale, String nome,String ip,String porta) throws CommandException {
        this(gestore, terminale, nome);
        this.ip = ip;
        this.porta = porta;
    }

    @Override
    public void execute() throws CommandException,ErrorLogException {
        try{
        if(ip.equals("") || porta == null)
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
