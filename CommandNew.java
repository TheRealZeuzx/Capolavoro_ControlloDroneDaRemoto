public class CommandNew extends CommandI<GestoreClientServer> implements UndoableCommand{

    public CommandNew(GestoreClientServer gestore) throws CommandException {
        super(gestore);
    }

    @Override
    public void execute() throws CommandException {
        
    }

    @Override
    public boolean undo() {
       
    }
    
}
