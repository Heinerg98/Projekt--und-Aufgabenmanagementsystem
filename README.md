# Projekt- und Aufgabenmanagementsystem

Einfache Full-Stack-Webanwendung für die IU-Fallstudie.

## Struktur

- `/backend`: Spring Boot (REST API, JPA/Hibernate, persistente H2-Dateidatenbank)
- `/frontend`: React (Vite, React Router)

## Backend starten

```bash
cd backend
mvn spring-boot:run
```

API läuft auf `http://localhost:8080`.

Die Anwendungsdaten werden persistent in einer H2-Datei gespeichert. Beim ersten Start werden Demo-Daten automatisch angelegt. Wenn du den Backend-Server wie unten gezeigt aus dem Verzeichnis `backend` startest, liegen die erzeugten Datenbankdateien im Unterordner `data/` dieses Verzeichnisses, also unter `<project-root>/backend/data/`. Zum Zurücksetzen kannst du diese Dateien löschen.

### Demo-Zugangsdaten

- `admin / admin123` (ADMIN)
- `leiter1 / leiter123` (PROJEKTLEITER)
- `mitarbeiter1 / mit123` (MITARBEITER)

## Frontend starten

```bash
cd frontend
npm install
npm run dev
```

Frontend läuft auf `http://localhost:5173`.

## Tests und Build

```bash
cd backend
mvn test

cd ../frontend
npm install
npm run build
```

## Wichtige Endpunkte

- `POST /api/auth/login`, `POST /api/auth/logout`, `GET /api/auth/me`
- `GET/POST/PUT /api/users` (Admin)
- `GET/POST/PUT /api/projects`, `POST /api/projects/{id}/archive`
- `POST/GET /api/projects/{id}/members`
- `GET/POST /api/projects/{id}/tasks`
- `GET/PUT /api/tasks/{taskId}`, `PATCH /api/tasks/{taskId}/status`
