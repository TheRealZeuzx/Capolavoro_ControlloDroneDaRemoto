public class Test{
    public static void main(String[] args) throws InterruptedException {
        try{
            Server server = new Server("test", 8086,new Terminal<Server>(new ErrorLog("logErrori.txt")));
            server.startTerminal();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void testServer(Server server){
        // tests
        return;
    }


}


