public class Test {
    public static void main(String[] args) {
        try{
            Server server = new Server("test", 8086);
            
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    private void testServer(Server server){
        // tests
        return;
    }

}


