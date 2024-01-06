import java.util.Scanner;

public class Terminal {

    private Error errorLog;
    private CommandHistory storiaComandi; //lo uso solo per i comandi di creazione e delete dei server-client perché per le altre op non ha senso.
    private GestoreClientServer gestore;

    public Terminal(String urlFileLogErrori){
        this.errorLog = new Error(urlFileLogErrori);
        this.storiaComandi = new CommandHistory();
        this.gestore = new GestoreClientServer();
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
                    "show\t\tpermette di visualizzare la lista di tutti i client e server creati\n\t\t(show all) per visualizzare lista di client e server,\n\t\t(show client) per visualizzare solo la lista di client,\n\t\t(show server) per visualizzare solo la lista di server\n"+
                    "info\t\tpermette di visualizzare le informazioni di uno specifico client o server (info nome)\n" +
                    "new \t\tpermette di creare un server o client specifico\n\t\t(new client nomeClient ip porta) l'ip e la porta si riferiscono al socket remoto destinatario\n\t\t(new server nomeServer porta) la porta si riferisce alla porta su cui creare la nuova Socket locale\n" +
                    "select\t\tpermette di selezionare un server o client in base al nome\n\t\t(select client nomeClient) permette di selezionare un client\n\t\t(select server nomeServer) permette di selezionare un server\n"+
                    "delete\t\tpermette di eliminare un server o client in base al nome\n\t\t(delete client nomeClient) permette di eliminare un client\n\t\t(delete server nomeServer) permette di eliminare un server\n" +
                    "undo\t\tpermette di annullare l'ultima operazione significativa eseguita (new e delete)\n"
                );
                break;
            case "show":
                String[] t = menu.toLowerCase().split(" ");
                String scelta = t.length <= 1  || t.length > 2 || t == null? "" : t[1];
                CommandShow comando = new CommandShow(gestore,scelta);
                if(comando == null || !comando.execute()){
                    System.out.println("errore, non è stato specificato cosa stampare");
                }
                break;
            case "quit":
                if(menu.equalsIgnoreCase("quit")){
                    System.out.println("Chiusura In Corso...");
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

        UndoableCommand command = storiaComandi.pop();
        if (command != null) {
            command.undo();
        }
    }

    private void executeCommand(UndoableCommand command) {
        if (command.execute()) {
            storiaComandi.push(command);
        }
    }
}
