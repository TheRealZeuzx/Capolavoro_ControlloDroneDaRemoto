public class ErrorLogCommand implements Command{

    private Error Elog;
    public ErrorLogCommand(Error Elog){

        this.Elog = Elog;
    }
    public void execute(){
        Elog.logToFile(msg);
    }
    public void undo(){

    }

}
