# PROJECTH

![GitHub repo size](https://img.shields.io/github/repo-size/Gotrik94/PROJECTH?style=flat-square)
![GitHub issues](https://img.shields.io/github/issues/Gotrik94/PROJECTH?style=flat-square)
![GitHub stars](https://img.shields.io/github/stars/Gotrik94/PROJECTH?style=flat-square)
![GitHub forks](https://img.shields.io/github/forks/Gotrik94/PROJECTH?style=flat-square)
![License](https://img.shields.io/github/license/Gotrik94/PROJECTH?style=flat-square)

## Prefazione

Il Progetto PROJECTH è stato concepito per creare una demo leggera e funzionale che si concentri su due componenti chiave:

- 🕹️ **Matchmaking**: per la gestione delle partite tra giocatori.
- 🔒 **Gestione delle sessioni**: per assicurare un'esperienza stabile e coerente.

L'obiettivo principale è costruire una base tecnologica solida per analizzare queste componenti essenziali, lasciando spazio a futuri sviluppi più complessi.

## Tecnologie Utilizzate

- 🗃️ **Database**: MySQL - Per la gestione dei dati persistenti (profili utente, statistiche di gioco, configurazioni di sistema).
- 💻 **Frontend**: Angular - Per creare un'interfaccia utente moderna e reattiva.
- 🔧 **Backend**: Java con Spring Boot - Per la logica applicativa, gestione delle sessioni e matchmaking.

## Funzionalità Principali

1. 🛠️ **Creazione del Personaggio**: I giocatori possono selezionare e personalizzare un personaggio utilizzando carte dalla propria libreria.
2. 🎴 **Meccaniche delle Carte**:
    - 🧮 Ogni carta ha un costo in punti.
    - 📏 I giocatori devono rispettare un punteggio massimo complessivo (es. 12 punti per personaggio).
    - ⚙️ Le combinazioni sono flessibili (es. 3 carte da 4 punti o 2 da 3 e 1 da 6 punti).
3. 🤖 **Simulazione Automatica del Match**:
    - 🎯 I benefici delle carte selezionate vengono applicati automaticamente.
    - 🩸 Il match termina quando uno dei due personaggi raggiunge 0 punti vita.

## Struttura del Progetto

### Frontend
- 🌐 Creato con **Angular** per garantire una navigazione fluida e un'esperienza utente intuitiva.

### Backend
- ⚙️ Implementato in **Java Spring Boot**, con un'attenzione particolare alla scalabilità e alla gestione delle sessioni.

### Database
- 🗂️ Il database **MySQL** contiene:
    - 🧑‍💻 Tabelle per i profili utente.
    - 🃏 Tabelle per le carte e le configurazioni dei personaggi.
    - 📊 Log delle partite e delle statistiche.

## Installazione

### Prerequisiti

- 🛠️ **Node.js** e **npm** (per il frontend Angular).
- ☕ **JDK 17+** (per il backend Java con Spring Boot).
- 🗃️ **MySQL** (per il database).

### Istruzioni

1. 🔄 Clona il repository:
    ```bash
    git clone https://github.com/Gotrik94/PROJECTH.git
    ```

2. 🗄️ Configura il database:
    - Il database verrà creato automaticamente al primo avvio del backend, quindi non è necessaria alcuna configurazione manuale iniziale.

3. 🚀 Avvia il backend:
    ```bash
    cd backend
    ./mvnw spring-boot:run
    ```

4. 💻 Avvia il frontend:
    ```bash
    cd frontend
    npm install
    ng serve
    ```

5. 🌍 Accedi all'applicazione all'indirizzo [http://localhost:4200](http://localhost:4200).

## Contribuire

Siamo entusiasti di ricevere contributi! Segui questi passaggi per contribuire:

1. 🍴 Fai un fork del repository.
2. 🛠️ Crea un branch per la tua feature o bugfix:
    ```bash
    git checkout -b feature/nome-feature
    ```
3. 📝 Fai un commit delle modifiche:
    ```bash
    git commit -m "Descrizione della modifica"
    ```
4. 🔀 Push del branch:
    ```bash
    git push origin feature/nome-feature
    ```
5. 📬 Apri una pull request.

## Licenza

Questo progetto è rilasciato sotto la licenza MIT. Per ulteriori dettagli, consulta il file LICENSE.

## Contatti

Per qualsiasi domanda o segnalazione, puoi contattare il team:
- 🐙 **GitHub Issues**: [Segnala un problema](https://github.com/Gotrik94/PROJECTH/issues)
- 🐙 **GitHub**: [Gotrik94](https://github.com/Gotrik94)

