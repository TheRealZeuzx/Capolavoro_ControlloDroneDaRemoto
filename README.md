# Controllo Drone Tello con Java

## La nostra applicazione
La nostra applicazione, sviluppata in Java, permette il controllo di un drone Tello tramite Socket UDP. Le principali funzionalità dell'applicazione includono:
- Istanziamento di Client e Server locali per permettere la comunicazione diretta con il drone (funzione di Client) e la ricezione passiva di informazioni dal drone (funzione di Server).
- Implementazione del design pattern COMMAND per organizzare i comandi inviati al drone.
- Integrazione con Maven per la gestione delle dipendenze.
- Utilizzo di librerie esterne (JavaCV) per ricevere lo streaming video direttamente dal drone.
- Funzione di Telecomando che apre un JFRAME con un KeyListener per i tasti WASD, che consente di controllare i movimenti del drone.

## La sua struttura
L'applicazione è strutturata in modo modulare per facilitare l'integrazione e la gestione dei componenti.
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
### Pro
**Modularità**: La chiara separazione tra UI, logica dell'applicazione e business logic facilita manutenzione e espansione.
**Scalabilità**: Nuove funzionalità possono essere aggiunte senza ridisegnare l'intero sistema, utile per sviluppi futuri.
**Flessibilità**: Possibilità di usare sia GUI, CLI, che qualsiasi altra UI, adattandosi a diverse esigenze degli utenti.
**Manutenibilità**: Pattern COMMAND e separazione tra componenti semplificano la manutenzione e l'integrazione di nuovi moduli.
### Contro
**Tempo di sviluppo**: Richiede un investimento significativo di tempo per creare moduli modulari e scalabili.
**Complessità**: La struttura complessa può aumentare il rischio di bug e richiede competenze avanzate.
**Risorse necessarie**: Necessita di competenze tecniche avanzate, specialmente nella programmazione di rete e gestione delle dipendenze.
**Scadenze strette**: Implementare un'architettura così complessa potrebbe non essere praticabile con tempi limitati, richiedendo soluzioni più semplici per rispettare le scadenze.

**Design Personalizzato per ogni I/O Device**
![image](https://github.com/TommasoMussaldi/ControlloSocketUDP_TPS/assets/96235087/d16f9591-7316-45eb-bc15-c233e03e2590)
### Pro
- **Gestione Indipendente delle Interfacce**: 
  - Permette di gestire ogni interfaccia (GUI o CLI) in modo indipendente, facilitando l'adattamento a diverse esigenze degli utenti e contesti di utilizzo.
- **Modularità**: 
  - La chiara separazione tra UI e business logic favorisce la modularità, consentendo di aggiornare o sostituire parti del sistema senza impattare altre componenti.
- **Flessibilità dell'Interfaccia Utente**: 
  - La possibilità di avere sia un'interfaccia grafica che un terminale aumenta la versatilità dell'applicazione, adattandosi a diversi scenari operativi.
- **Manutenibilità**: 
  - L'utilizzo del pattern COMMAND per gestire i comandi e la separazione tra componenti semplificano la manutenzione e l'aggiornamento del codice.
- **Scalabilità**: 
  - La struttura è progettata per essere scalabile, permettendo l'aggiunta di nuove funzionalità senza dover ridisegnare l'intero sistema.

### Contro
- **Tempo di Sviluppo**: 
  - Implementare questa architettura richiede un significativo investimento di tempo, soprattutto nella creazione di moduli altamente modulari e scalabili.
- **Complessità**: 
  - La complessità dell'architettura può rappresentare una sfida, specialmente se il team non ha esperienza pregressa con design pattern come COMMAND, aumentando il rischio di bug nella fase iniziale.
- **Interdipendenza tra UI e Business Logic**: 
  - Nonostante la separazione, non è possibile suddividere completamente la business logic e parte dei comandi dalla UI, portando a una certa interdipendenza che può complicare la gestione del codice.
- **Risorse Necessarie**: 
  - Richiede competenze tecniche avanzate per gestire la comunicazione tra moduli, specialmente nella programmazione di rete e nella gestione delle dipendenze.
- **Scadenze Strette**: 
  - Con tempi limitati, implementare un'architettura così complessa potrebbe non essere praticabile, richiedendo soluzioni più semplici per rispettare le scadenze.

**Design Finale che vorremmo implementare**
![image](https://github.com/TommasoMussaldi/ControlloSocketUDP_TPS/assets/96235087/edb4a3d0-862f-4911-b83a-380ceaffb51b)
### Pro
- **Separazione dei Concerns**:
  - La completa separazione tra UI e applicazione (business logic e librerie) garantisce una chiara distinzione tra le responsabilità dei diversi moduli, facilitando la gestione e la manutenzione del codice.
- **Indipendenza dei Moduli**:
  - Ogni componente può essere sviluppato, testato e aggiornato indipendentemente dagli altri, migliorando la modularità e riducendo il rischio di regressioni.
- **Comunicazione Efficace tramite Observer**:
  - L'implementazione del design pattern Observer permette di notificare in tempo reale gli altri componenti in caso di eventi critici, come la disconnessione di un client o un server, migliorando la robustezza e la reattività del sistema.
- **Riutilizzabilità dei Componenti con Mediator**:
  - L'uso del design pattern Mediator facilita il riutilizzo dei componenti, rendendo la UI un mediatore e ogni componente un element, semplificando l'interazione tra di essi e migliorando l'estensibilità del sistema.
- **Flessibilità dell'Interfaccia Utente**:
  - La possibilità di gestire diverse interfacce (GUI e terminale) senza modificare la logica di business rende il sistema versatile e adattabile a vari scenari operativi.
- **Scalabilità**:
  - La struttura modulare permette di aggiungere nuove funzionalità e componenti senza necessità di ridisegnare l'intero sistema, facilitando la crescita e l'evoluzione del progetto.

### Contro
- **Aumento della Complessità del Sistema**:
  - La completa separazione e l'implementazione dei design pattern Observer e Mediator aumentano la complessità del sistema, richiedendo una maggiore attenzione nella progettazione e nello sviluppo.
- **Overhead di Comunicazione**:
  - L'introduzione di un componente interfaccia per la comunicazione tra UI e applicazione può creare un overhead nella gestione dei dati e dei comandi, potenzialmente introducendo latenza.
- **Maggiore Sforzo di Manutenzione**:
  - La necessità di mantenere sincronizzati i componenti indipendenti e gestire le notifiche degli eventi aumenta il carico di manutenzione, richiedendo un monitoraggio costante e aggiornamenti frequenti.
- **Risorse e Tempo di Sviluppo**:
  - Implementare e testare questa architettura richiede un significativo investimento di risorse e tempo, rallentando il progresso iniziale e aumentando il carico di lavoro del team.
- **Integrazione e Test**:
  - La separazione dei moduli comporta sfide nell'integrazione e nei test, richiedendo un piano di integrazione ben strutturato e test approfonditi per assicurare che tutte le parti funzionino armoniosamente.

## I nostri piani futuri
- **Interfacce intercambiabili:** Vogliamo sviluppare l'applicazione in modo che le interfacce utente possano essere facilmente scambiate. Questo permetterà agli sviluppatori di creare nuove interfacce utente semplicemente chiamando i metodi dedicati (ad esempio, `muoviDestra()`, `capriola()`).
- **Trasformazione in libreria:** L'obiettivo a lungo termine è trasformare la nostra app in una libreria. Questo consentirà agli sviluppatori di integrare facilmente il controllo del drone Tello nelle proprie applicazioni, fornendo una maggiore flessibilità e usabilità.

Con il tempo e le risorse adeguate, ci proponiamo di migliorare e ampliare le funzionalità dell'applicazione, rendendola uno strumento versatile e potente per il controllo del drone Tello.

---
