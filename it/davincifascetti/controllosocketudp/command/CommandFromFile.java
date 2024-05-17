package it.davincifascetti.controllosocketudp.command;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Client;

/**
 * CLIENT 
 * permette di avviare leggere un file dal client e inviarre il contenuto al server
 *  @author Mussaldi Tommaso, Mattia Bonfiglio
 *  @version 1.0
 */
public class CommandFromFile extends CommandI<Client>{
    /**
     * @param nomeFile nome del file da cui leggere i dati
     * @param gestore client receiver
     * @throws CommandException
     */
    public CommandFromFile(Client gestore,String nomeFile) throws CommandException {
        super(gestore,nomeFile);
    }

    @Override
    public void execute() throws CommandException, ErrorLogException{
        FileReader fileTesto;
        try{
            fileTesto = new FileReader(this.getParams());
        }catch(FileNotFoundException e){
            throw new ErrorLogException(e.getMessage());
        }
        Scanner in = new Scanner(fileTesto);  
        String contenutoFile = "";
        while(in.hasNextLine()) {
            contenutoFile += in.nextLine() +"\n";
        }

        in.close();
        try {
            fileTesto.close();
        } catch (IOException e) {
            throw new ErrorLogException(e.getMessage());
        }
        try {
            this.getGestore().inviaMsg("print " + contenutoFile);
        } catch (CommandableException e) {
            throw new CommandException(e.getMessage());
        }
    }
}

