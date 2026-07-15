# TESTING

## 📋 Test-Klassen & abgedeckte Bereiche

### Backend (JUnit Integration Tests)
- `AuthControllerTest` (6 Tests)
  - Login erfolgreich/fehlgeschlagen
  - Logout + Token-Invalidierung
  - Zugriff ohne Token
  - Zugriff mit abgelaufenem Token
- `ProjectControllerTest` (8 Tests)
  - Projektanlage RBAC
  - Sichtbarkeit nach Rolle (ADMIN / PROJEKTLEITER / MITARBEITER)
  - Archivierung RBAC
  - Fortschrittsberechnung (`totalTasks`, `completedTasks`, `progress`)
- `TaskControllerTest` (7 Tests)
  - Task-Erstellung RBAC
  - Statuswechsel `OFFEN -> IN_BEARBEITUNG -> ERLEDIGT`
  - Task-Projektzuordnung
  - Validierung ungültiger Daten
- `ProjectMemberTest` (5 Tests)
  - Mitglieder hinzufügen RBAC
  - Sichtbarkeit nach Hinzufügen
  - Doppeltes Hinzufügen (idempotent)
  - Ungültige User-ID
- `UserControllerTest` (6 Tests)
  - Admin-Useranlage + Rollenvergabe
  - RBAC für User-Verwaltung
  - Pflichtfeldvalidierung
  - Doppelter Username
  - Ungültiges E-Mail-Format

**Gesamt:** 32 Integrationstests im Backend.

## ✅ Test-Ergebnisse

Ausgeführt:
```bash
cd backend && mvn test
```
Ergebnis:
- Tests run: **32**
- Failures: **0**
- Errors: **0**
- Skipped: **0**
- Status: **BUILD SUCCESS**

Zusätzliche Validierung:
```bash
cd frontend && npm install && npm run build
```
Ergebnis:
- Frontend-Build erfolgreich

## 💾 Persistenz

- Die Anwendung verwendet im Normalbetrieb eine **persistente dateibasierte H2-Datenbank** (`jdbc:h2:file:./data/pmsdb`).
- Beim ersten Start werden Demo-Daten automatisch angelegt.
- Die Integrationstests nutzen weiterhin eine **isolierte In-Memory-H2-Datenbank** mit Testdaten aus `backend/src/test/resources/test-data.sql`.

## 🔐 Sicherheits-Checkliste

- [x] Passwort-Hashing mit BCrypt (`PasswordEncoder` in `SecurityConfig`)
- [x] Login verifiziert Passwort über `passwordEncoder.matches(...)` in `AuthService`
- [x] `UserService` hasht Passwörter bei Create/Update
- [x] `test-data.sql` enthält BCrypt-gehashte Test-Passwörter (`{bcrypt}...`)
- [x] Unautorisierter Zugriff ohne/ungültigen/abgelaufenen Token liefert HTTP 401
- [x] RBAC-Fälle (ADMIN / PROJEKTLEITER / MITARBEITER) durch Integrationstests abgesichert

## 📊 Testdaten-Übersicht (Testprofil H2 `test-data.sql`)

- **User:** 6 (`admin`, `leiter1`, `leiter2`, `mitarbeiter1`, `mitarbeiter2`, `mitarbeiter3`)
- **Projekte:** 4 (davon 3 aktiv, 1 archiviert)
- **Projektmitglieder:** 4 Zuordnungen
- **Tasks:** 6 mit gemischten Status (`OFFEN`, `IN_BEARBEITUNG`, `ERLEDIGT`)

## 🚀 Tests ausführen

```bash
cd backend
mvn clean test
```
