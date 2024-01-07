public class CommandDefault implements Command{
    private String msg;
    public CommandDefault(String[] params) throws CommandException{
        this.msg = "";
        if(params != null && params.length != 0){
            for (String string : params) {
                this.msg += string;
            }
        }
    }
    public boolean execute() {
        System.out.println("il comando '"+ this.msg + "' non è riconosciuto");
        return false;
    }
}
