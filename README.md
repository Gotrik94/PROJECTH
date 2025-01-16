# PROJECTH

![GitHub repo size](https://img.shields.io/github/repo-size/Gotrik94/PROJECTH?style=flat-square)
![GitHub issues](https://img.shields.io/github/issues/Gotrik94/PROJECTH?style=flat-square)
![GitHub stars](https://img.shields.io/github/stars/Gotrik94/PROJECTH?style=flat-square)
![GitHub forks](https://img.shields.io/github/forks/Gotrik94/PROJECTH?style=flat-square)
![License](https://img.shields.io/github/license/Gotrik94/PROJECTH?style=flat-square)

ProjectH è un progetto concepito per realizzare una demo leggera e funzionale, pensata per esplorare e testare sistemi complessi come il matchmaking e la gestione delle sessioni in un contesto multiplayer. Il focus principale è sulla creazione di una base tecnologica solida, piuttosto che sulla complessità del gameplay. 🌐

## Obiettivi del Progetto 💡

- Testare la gestione delle sessioni multiplayer.
- Implementare un sistema di matchmaking efficiente.
- Creare un prototipo dimostrativo semplice e funzionale.

## Tecnologie Utilizzate

- **Backend:** Java con Spring Boot
- **Database:** MySQL

## Caratteristiche Principali

- **Sistema di matchmaking:** Permette agli utenti di mettersi in coda per un match.
- **Personalizzazione dei personaggi:** Gli utenti possono selezionare un personaggio e personalizzarlo utilizzando una libreria di elementi con punteggi predefiniti.
- **Simulazione degli incontri:** Gli incontri vengono gestiti automaticamente dal sistema.
- **Gestione dei dati persistenti:** Utilizzo di MySQL per memorizzare profili utente, statistiche di gioco e configurazioni.

## Come Clonare il Progetto

```bash
git clone https://github.com/Gotrik94/PROJECTH.git
```

## Requisiti di Sistema 🔢

- **Java:** Versione 17 o superiore
- **MySQL:** Versione 8 o superiore

## Configurazione del Progetto

1. Configurare il database:

   - Creare uno schema MySQL denominato `projecth` utilizzando il comando:
     ```sql
     CREATE SCHEMA projecth;
     ```
   - Configurare le credenziali di accesso al database nel file `application.properties` del backend.

2. Avviare il backend:

   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
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

