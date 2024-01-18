package it.davincifascetti.controllosocketudp.program;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;

public class Test{
    public static void main(String[] args) throws InterruptedException {
        try{
            Server server = new Server("test", "8086",new Terminal<Server>(new ErrorLog("logErrori.txt")));
            server.startTerminal();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /*
     * new c c1 localhost 1212
     * new s s1 1212
     * select c c1
     */

    private void testServer(Server server){
        // tests
        return;
    }


}


