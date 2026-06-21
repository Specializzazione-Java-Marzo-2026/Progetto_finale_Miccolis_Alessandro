## Configurazione SQLTools (MySQL/MariaDB) - Workspace

Ho aggiunto la configurazione workspace per SQLTools e script per eseguire i file SQL.

- Estensioni raccomandate (apri il pannello Estensioni in VS Code):
  - `mtxr.sqltools`
  - `mtxr.sqltools-driver-mysql`

- File creati:
  - [.vscode/extensions.json](.vscode/extensions.json) — raccomandazioni estensioni
  - [.vscode/settings.json](.vscode/settings.json) — connessione `Progetto_finale_local` per SQLTools
  - [scripts/run_sql_on_mysql.ps1](scripts/run_sql_on_mysql.ps1) — script PowerShell per eseguire `drop/create/insert`
  - [scripts/run_sql_on_mysql.sh](scripts/run_sql_on_mysql.sh) — script bash equivalente

I valori usati per la connessione (come richiesto):

- Host: `localhost`
- Port: `3306`
- Database: `Progetto_finale`
- Username: `root`
- Password: `root`

Come procedere:

1. Installa le estensioni raccomandate (puoi aprire il workspace e cliccare su "Install" nel pannello Estensions).
2. Riavvia VS Code dopo l'installazione (consigliato).
3. Apri il pannello `SQLTools` (icona DB) e verifica che `Progetto_finale_local` sia visibile.
4. Clic destro su `Progetto_finale_local` → `Test Connection`.
5. Apri [sql/drop.sql](sql/drop.sql), imposta la connessione attiva (click destro sulla connessione → `Use Connection`) e `Run on active connection`.

Se non hai `mysql` nella PATH, usa gli script in `scripts/` passando le credenziali, ad esempio in PowerShell:

```powershell
.
\scripts\run_sql_on_mysql.ps1 -Host localhost -Port 3306 -Database Progetto_finale -Username root -Password root
```

Oppure in bash:

```bash
./scripts/run_sql_on_mysql.sh localhost 3306 Progetto_finale root root
```

Sicurezza: i file `.vscode/settings.json` e gli script contengono credenziali in chiaro; sono pensati per uso locale. Se condividi il repo, rimuovi o modifica queste impostazioni.
