import java.util.Scanner;

public class Terminal {

    private CommandHistory storiaComandi;
    private Error errorLog;
    private Server []listaServer;
    private Client []listaClient;

    public Terminal(String urlFileLogErrori){
        this.storiaComandi = new CommandHistory();
        this.errorLog = new Error(urlFileLogErrori);
        this.listaServer = new Server[10];
        this.listaClient = new Client[10];
    }

    public void main() {
        
        String menu;
        Scanner input = new Scanner(System.in);
        System.out.println("Inizializzazione completata, terminale attivo");
        do{
            System.out.print(">");
            menu = input.nextLine();
            
            String temp;
            if(menu.isBlank())
                temp = "";
            //split crea un array in cui inserisce le parole separate dalla stringa inserita, in questo caso " "
            else temp = menu.toLowerCase().split(" ")[0];
            
            //?Usare command anche nei comandi della console (penso di si)
            switch(temp){ 
            case "h":
            case "?":
            case "help":
                System.out.println(
                    "-------------------------------------------------------------------------------\n"+
                    "Comandi Terminale Generale\n\n"+
                    "help\t\tpermette di visualizzare tutti i comandi \n" + 
                    "quit\t\tpermette di terminare l'esecuzione \n" + 
                    "show\t\tpermette di visualizzare la lista di tutti i client e server creati\n\t\t(show client) per visualizzare solo la lista di client,\n\t\t(show server) per visualizzare solo la lista di server\n"+
                    "info\t\tpermette di visualizzare le informazioni di uno specifico client o server (info nome)\n" +
                    "new \t\tpermette di creare un server o client specifico\n\t\t(new client nomeClient ip porta) l'ip e la porta si riferiscono al socket remoto destinatario\n\t\t(new server nomeServer porta) la porta si riferisce alla porta su cui creare la nuova Socket locale\n" +
                    "select\t\tpermette di selezionare un server o client in base al nome\n\t\t(select client nomeClient) permette di selezionare un client\n\t\t(select server nomeServer) permette di selezionare un server\n"+
                    "delete\t\tpermette di eliminare un server o client in base al nome\n\t\t(delete client nomeClient) permette di eliminare un client\n\t\t(delete server nomeServer) permette di eliminare un server\n"
                );
                break;
            case "show":
                //TODO mostra tutti i client e server oppure se specificato show client solo i client oppure solo i server con show server
                //TODO controlla che dopo show ci siano effettivamente scritte cose sensate per il comando
                break;
            case "quit":
                if(menu.equalsIgnoreCase("quit")){
                    System.out.println("Chiusura Terminale In Corso...");
                    break;
                }
            default:
                System.out.println("il comando '" + menu + "' non è riconosciuto");
                break;
            }
        }while(!menu.equalsIgnoreCase("quit"));
        
    }

    private void undo() {
        if (storiaComandi.isEmpty()) return;

        Command command = storiaComandi.pop();
        if (command != null) {
            command.undo();
        }
    }

    private void executeCommand(Command command) {
        if (command.execute()) {
            storiaComandi.push(command);
        }
    }
}
