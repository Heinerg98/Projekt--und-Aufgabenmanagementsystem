<<<<<<< HEAD
# Projekt--und-Aufgabenmanagementsystem

Minimale Full-Stack-Implementierung mit Spring Boot (Backend), React (Frontend), RBAC und H2.

## Struktur

- `/backend`: Spring Boot API mit Entities, Services, Controllern und JUnit-Integrationstests
- `/frontend`: React-App mit Router, AuthContext und Protected Routes

## Backend (Qualitätsprüfung)

### Validierte Domänenmodelle
- **Entities**: `User`, `Project`, `ProjectMember`, `Task`
- **Enums**: `Role`, `ProjectStatus`, `TaskStatus`
- **Constraints/Defaults**:
  - eindeutiger Username
  - unique ProjectMember (`project_id`,`user_id`)
  - Status-/Zeitstempel-Defaults via `@PrePersist`

### RBAC in Services
- **ADMIN**: Benutzer erstellen, Rollen vergeben, alle Projekte sehen
- **PROJEKTLEITER**: Projekte erstellen/archivieren, Mitglieder hinzufügen, Fortschritt sehen
- **MITARBEITER**: Aufgaben nur in berechtigten Projekten erstellen/bearbeiten

### Authentifizierung
- Token-basiert über Header **`X-Auth-Token`**
- Endpunkte: `/api/auth/login`, `/api/auth/logout`, `/api/auth/me`
- Token-Ablauf wird auf `401 Unauthorized` getestet

## JUnit-Testabdeckung (konkrete Szenarien)

Dateien:
- `backend/src/test/java/com/itcompany/pms/controller/AuthAndProjectIntegrationTest.java`
- `backend/src/test/java/com/itcompany/pms/controller/TokenExpiryIntegrationTest.java`

Abgedeckte Szenarien:
- Auth: Login Erfolg/Misserfolg, Logout invalidiert Token, Token-Expired -> 401
- Projekte: Erstellen (Leader erlaubt, Mitarbeiter verboten), Sichtbarkeit Leader/Admin, Archivierung nur verantwortlicher Leader
- Aufgaben: Erstellen im eigenen/fremden Projekt, Statuswechsel OFFEN -> IN_BEARBEITUNG -> ERLEDIGT, Fortschritt in %
- Mitglieder: Hinzufügen durch Leader, neue Projektsichtbarkeit, Verbot für Nicht-Leader
- Admin: Benutzer anlegen, Rolle zuweisen, Verbot für Nicht-Admin

## Integrationstest-Workflow
- Vollständige Kette über API getestet: Login -> Projekt/Mitglied/Aufgabe -> Fortschritt -> Admin-Verwaltung
- Multi-User-Szenarien mit Rollen `ADMIN`, `PROJEKTLEITER`, `MITARBEITER`

## Frontend-Struktur & Testfälle

Umgesetzt:
- React Router (`/login`, `/dashboard`)
- `AuthContext` für globalen Login-Status + Token-Speicherung
- `ProtectedRoute` für geschützte Navigation
- Rollenbasierte UI-Elemente im Dashboard

Manuelle Frontend-Testfälle:
1. Login mit gültigen Demo-Credentials führt zu Dashboard
2. Token/Role werden in `localStorage` abgelegt
3. Dashboard zeigt rollenbasiert erlaubte Projekte
4. Rollenbasierte Buttons (`Projekt erstellen`, `Benutzer verwalten`) werden korrekt angezeigt

## H2-Testdatensätze

`backend/src/main/resources/data.sql` enthält:
- 1 Admin, 2 Projektleiter, 3 Mitarbeitende
- mehrere Projekte mit Status `AKTIV`/`ARCHIVIERT`
- Aufgaben mit `OFFEN`, `IN_BEARBEITUNG`, `ERLEDIGT`

## Ausführung

Backend testen:
```bash
cd backend
mvn test
```

Frontend bauen:
```bash
cd frontend
npm install
npm run build
```

## Ergebnis

- `mvn test`: **17/17 Tests erfolgreich**
- `npm run build`: **erfolgreich**
- während der Prüfung identifizierte Probleme (ID-Sequenzen, veraltete Router-Version) wurden behoben.
=======
# Projekt- und Aufgabenmanagementsystem

Einfache Full-Stack-Webanwendung für die IU-Fallstudie.

## Struktur

- `/backend`: Spring Boot (REST API, JPA/Hibernate, H2)
- `/frontend`: React (Vite, React Router)

## Backend starten

```bash
cd backend
mvn spring-boot:run
```

API läuft auf `http://localhost:8080`.

### Test-Logins

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

## Wichtige Endpunkte

- `POST /api/auth/login`, `POST /api/auth/logout`, `GET /api/auth/me`
- `GET/POST/PUT /api/users` (Admin)
- `GET/POST/PUT /api/projects`, `POST /api/projects/{id}/archive`
- `POST/GET /api/projects/{id}/members`
- `GET/POST /api/projects/{id}/tasks`
- `GET/PUT /api/tasks/{taskId}`, `PATCH /api/tasks/{taskId}/status`
>>>>>>> origin/main
