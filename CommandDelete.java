public class CommandDelete extends CommandI<GestoreClientServer> implements UndoableCommand{

    public CommandDelete(GestoreClientServer gestore) throws CommandException {
        super(gestore);
    }
    @Override
    public void execute() throws CommandException {
        
    }
    @Override
    public boolean undo() {

    }
}
