# Controllo Drone Tello con Java

## La nostra applicazione

La nostra applicazione, sviluppata in Java, permette il controllo di un drone Tello tramite Socket UDP. Le principali funzionalità dell'applicazione includono:
- Istanziamento di Client e Server locali per permettere la comunicazione diretta con il drone (funzione di Client) e la ricezione passiva di informazioni dal drone (funzione di Server).
- Implementazione del design pattern COMMAND per organizzare i comandi inviati al drone.
- Integrazione con Maven per la gestione delle dipendenze.
- Utilizzo di librerie esterne (JavaCV) per ricevere lo streaming video direttamente dal drone.
- Funzione di Telecomando che apre un JFRAME con un KeyListener per i tasti WASD, che consente di controllare i movimenti del drone.

## La sua struttura

L'applicazione è strutturata in modo modulare per facilitare l'integrazione e la gestione dei componenti. Ecco una panoramica della struttura:

- **Client e Server:** Permettono la comunicazione bidirezionale con il drone. Il Client invia i comandi mentre il Server riceve i dati dal drone.
- **Pattern COMMAND:** Utilizzato per organizzare e gestire i comandi inviati al drone, migliorando la manutenibilità e l'estensibilità del codice.
- **Maven:** Gestisce le dipendenze dell'applicazione, facilitando l'integrazione di librerie esterne come JavaCV.
- **Streaming Video:** Grazie a JavaCV decodifichiamo i frame video ricevuti dal drone (codificati in H264), per cui l'applicazione è in grado di ricevere e visualizzare lo streaming video dal drone.
- **Telecomando:** Un JFRAME con un KeyListener per i tasti WASD permette di controllare il drone in tempo reale.

## Cosa potevamo fare meglio

Durante lo sviluppo dell'applicazione, abbiamo incontrato alcune difficoltà e identificato aree di miglioramento:

- **Struttura iniziale:** Inizialmente avevamo pensato di avere un terminale singolo simile ad una CLI per l'I/O con l'utente. Tuttavia, questo approccio avrebbe causato problemi in caso di sviluppo di diverse GUI.
- **Modularità delle interfacce:** Abbiamo sviluppato uno schema per rendere le varie classi di I/O intercambiabili come mattoncini Lego. Questo avrebbe permesso di integrare facilmente nuove interfacce con l'applicazione. Purtroppo, per ragioni di tempistica, non siamo riusciti a implementare questa struttura.

![image](https://github.com/TommasoMussaldi/ControlloSocketUDP_TPS/assets/96235087/ba4d15d9-97c2-4bb0-b189-8a881ef469fd)


## Possibili Design
**Design Interfaccia Generale verso l'esterno**
![image](https://github.com/TommasoMussaldi/ControlloSocketUDP_TPS/assets/96235087/982ea5b4-b5fb-4a65-b7fe-0714f637f8be)

// problemi con questo design


**Design Personalizzato per ogni I/O Device**
![image](https://github.com/TommasoMussaldi/ControlloSocketUDP_TPS/assets/96235087/d16f9591-7316-45eb-bc15-c233e03e2590)

// problemi con questo design

**Design Finale che vorremmo implementare**
Per far si che tutti sappiano quando un client o un server muore, possiamo implementare l’observer design pattern.
Per far si che i componenti siano facilmente riutilizzabili, possiamo rendere la UI un mediator e poi ogni componente un Component applicando il Mediator design pattern.

// problemi con questo design
// pro
![image](https://github.com/TommasoMussaldi/ControlloSocketUDP_TPS/assets/96235087/edb4a3d0-862f-4911-b83a-380ceaffb51b)




## I nostri piani futuri

Abbiamo piani ambiziosi per il futuro dell'applicazione:

- **Interfacce intercambiabili:** Vogliamo sviluppare l'applicazione in modo che le interfacce utente possano essere facilmente scambiate. Questo permetterà agli sviluppatori di creare nuove interfacce utente semplicemente chiamando i metodi dedicati (ad esempio, `muoviDestra()`, `capriola()`).
- **Trasformazione in libreria:** L'obiettivo a lungo termine è trasformare la nostra app in una libreria. Questo consentirà agli sviluppatori di integrare facilmente il controllo del drone Tello nelle proprie applicazioni, fornendo una maggiore flessibilità e usabilità.

Con il tempo e le risorse adeguate, ci proponiamo di migliorare e ampliare le funzionalità dell'applicazione, rendendola uno strumento versatile e potente per il controllo del drone Tello.

---
