package it.davincifascetti.controllosocketudp.program;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

//! leggi
//TODO possibilità da parte del client di cambiargli mod?
/**si occupa di prendere il pacchetto ricevuto dal server e elaborare la risposta corretta
 * ha una serie di parametri booleani: stampaVideo,stampaFile,Telecomando,
 * di default stampaVideo è attivo quindi stampa a video tutti i msg ricevuti dal client
 * 
 */
public class ServerThread implements Runnable{
    private static final int LunghezzaBuffer = 1024;
    private DatagramPacket packet;
    private DatagramSocket socketRisposta;
    private ArrayList<String> StoriaMsg;
    private Terminal<Server> riferimentoTerminal;
    //TODO creare i seguenti metodi per eseguire le op di :
    //TODO stampa msg del  client sulla console (se il terminale è attivo altrimenti li salva su storiamsg)
    //TODO salva su file msg client

    public ServerThread(DatagramPacket packet, DatagramSocket socketRisposta, ArrayList<String> StoriaMsg, Terminal<Server> riferimentoTerminal){
        this.packet = packet;
        this.socketRisposta = socketRisposta;
        this.StoriaMsg = StoriaMsg;
        this.riferimentoTerminal = riferimentoTerminal;
    }

    @Override
    public void run() {
        //estraggo il contenuto testuale
		int lungPacket = this.packet.getLength();
		String msgRicevuto = new String(this.packet.getData());
		msgRicevuto = msgRicevuto.substring(0, lungPacket);

		byte[] bufferOUT = new byte[ServerThread.LunghezzaBuffer];
        //TODO in base al tipo//TODO in base al tipo di parametro decide se stampare a video, su file o Telecomando
        
        if(this.riferimentoTerminal.isAttivo())System.out.println(msgRicevuto);
		else this.StoriaMsg.add(msgRicevuto);
    }

    // private boolean writeToFile(String msgRicevuto){
    //     return true;
    // }

    
}



