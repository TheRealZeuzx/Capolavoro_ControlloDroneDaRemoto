public class CommandNew extends CommandI<GestoreClientServer> implements UndoableCommand{

    private String nome;
    public CommandNew(GestoreClientServer gestore,String nome) throws CommandException {
        super(gestore);
    }

    @Override
    public void execute() throws CommandException {
        //this.getGestore().creaClient(, false);
    }

    @Override
    public boolean undo() {
       return true;
    }
    
}
