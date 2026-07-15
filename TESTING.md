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
- `TaskControllerTest` (6 Tests)
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

**Gesamt:** 31 Integrationstests im Backend.

## ✅ Test-Ergebnisse

Ausgeführt:
```bash
cd backend && mvn test
```
Ergebnis:
- Tests run: **31**
- Failures: **0**
- Errors: **0**
- Skipped: **0**
- Status: **BUILD SUCCESS**

Zusätzliche Validierung:
```bash
cd frontend && npm run build
```
Ergebnis:
- Frontend-Build erfolgreich

## 🔐 Sicherheits-Checkliste

- [x] Passwort-Hashing mit BCrypt (`PasswordEncoder` in `SecurityConfig`)
- [x] Login verifiziert Passwort über `passwordEncoder.matches(...)` in `AuthService`
- [x] `UserService` hasht Passwörter bei Create/Update
- [x] `data.sql` enthält BCrypt-gehashte Test-Passwörter (`{bcrypt}...`)
- [x] Unautorisierter Zugriff ohne/ungültigen/abgelaufenen Token liefert HTTP 401
- [x] RBAC-Fälle (ADMIN / PROJEKTLEITER / MITARBEITER) durch Integrationstests abgesichert

## 📊 Testdaten-Übersicht (H2 `data.sql`)

- **User:** 6 (`admin`, `leiter1`, `leiter2`, `mitarbeiter1`, `mitarbeiter2`, `mitarbeiter3`)
- **Projekte:** 4 (davon 3 aktiv, 1 archiviert)
- **Projektmitglieder:** 4 Zuordnungen
- **Tasks:** 6 mit gemischten Status (`OFFEN`, `IN_BEARBEITUNG`, `ERLEDIGT`)

## 🚀 Tests ausführen

```bash
cd backend
mvn clean test
```
