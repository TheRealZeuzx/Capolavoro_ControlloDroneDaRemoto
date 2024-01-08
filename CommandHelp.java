public class CommandHelp implements Command{
    private String msg;
    public CommandHelp(String msg){
        this.msg = msg;
    }
    public boolean execute() {
        System.out.println(this.msg);
        return true;
    }
}
