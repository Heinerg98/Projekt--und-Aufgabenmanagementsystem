# Projekt- und Aufgabenmanagementsystem

Full-Stack-Webanwendung zur zentralen Verwaltung von Projekten, Aufgaben und Mitarbeitenden – entwickelt im Rahmen einer IU-Fallstudie für einen mittelständischen IT-Dienstleister. Die Anwendung ersetzt die bisherige Verwaltung in Excel-Tabellen durch eine rollenbasierte Web-Lösung mit Authentifizierung, Autorisierung und persistenter Datenspeicherung.

## Technologiestack

- **Frontend:** React (Vite, React Router)
- **Backend:** Spring Boot (REST API)
- **Persistenz:** JPA/Hibernate mit H2-Dateidatenbank

## Struktur

- `/backend`: Spring Boot (REST API, JPA/Hibernate, persistente H2-Dateidatenbank)
- `/frontend`: React (Vite, React Router)

## Backend starten

```bash
cd backend
mvn spring-boot:run
```

API läuft auf `http://localhost:8080`.

Die Anwendungsdaten werden persistent in einer H2-Datei gespeichert (`data/pmsdb`). Beim ersten Start werden automatisch Demo-Daten angelegt.

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

Die Anwendung wird durch 32 automatisierte JUnit-Integrationstests im Backend (Authentifizierung, Projekt-, Aufgaben-, Mitglieder- und Benutzerverwaltung) sowie einen erfolgreichen Frontend-Build abgesichert. Details siehe [TESTING.md](./TESTING.md).

## Wichtige Endpunkte

- `POST /api/auth/login`, `POST /api/auth/logout`, `GET /api/auth/me`
- `GET/POST/PUT /api/users` (Admin)
- `GET/POST/PUT /api/projects`, `POST /api/projects/{id}/archive`
- `POST/GET /api/projects/{id}/members`
- `GET/POST /api/projects/{id}/tasks`
- `GET/PUT /api/tasks/{taskId}`, `PATCH /api/tasks/{taskId}/status`
