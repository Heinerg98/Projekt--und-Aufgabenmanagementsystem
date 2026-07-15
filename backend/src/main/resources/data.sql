<<<<<<< HEAD
INSERT INTO users (id, username, password, role, active, created_at) VALUES
(1, 'admin', '$2b$10$EucFAprqB.fb.sPVDnI9jeBM7Z04KIGMrGUHJn.e08VnI5TZd.59q', 'ADMIN', true, CURRENT_TIMESTAMP()),
(2, 'leiter1', '$2b$10$DIG769oq4lnDw.SG.7r92OXG13iBw5L.Q3xyTWYVTrV/sHd8GVwFa', 'PROJEKTLEITER', true, CURRENT_TIMESTAMP()),
(3, 'leiter2', '$2b$10$DIG769oq4lnDw.SG.7r92OXG13iBw5L.Q3xyTWYVTrV/sHd8GVwFa', 'PROJEKTLEITER', true, CURRENT_TIMESTAMP()),
(4, 'mitarbeiter1', '$2b$10$FOa8der5uAJHDy6KbojTOO0uyO9M.T8kzZUqZ5QPUxIYEAhxslq7W', 'MITARBEITER', true, CURRENT_TIMESTAMP()),
(5, 'mitarbeiter2', '$2b$10$FOa8der5uAJHDy6KbojTOO0uyO9M.T8kzZUqZ5QPUxIYEAhxslq7W', 'MITARBEITER', true, CURRENT_TIMESTAMP()),
(6, 'mitarbeiter3', '$2b$10$FOa8der5uAJHDy6KbojTOO0uyO9M.T8kzZUqZ5QPUxIYEAhxslq7W', 'MITARBEITER', true, CURRENT_TIMESTAMP());

INSERT INTO projects (id, name, description, project_lead_id, status, created_at) VALUES
(1, 'CRM-Migration', 'Migration auf neues CRM', 2, 'AKTIV', CURRENT_TIMESTAMP()),
(2, 'Monitoring-Optimierung', 'Dashboards und Alerts', 2, 'AKTIV', CURRENT_TIMESTAMP()),
(3, 'Legacy-Abbau', 'Abschaltung Altsystem', 3, 'ARCHIVIERT', CURRENT_TIMESTAMP());

INSERT INTO project_members (id, project_id, user_id, joined_at) VALUES
(1, 1, 4, CURRENT_TIMESTAMP()),
(2, 1, 5, CURRENT_TIMESTAMP()),
(3, 2, 5, CURRENT_TIMESTAMP()),
(4, 3, 6, CURRENT_TIMESTAMP());

INSERT INTO tasks (id, title, description, project_id, created_by_id, assigned_to_id, status, created_at) VALUES
(1, 'API Analyse', 'Bestehende Schnittstellen prüfen', 1, 4, 4, 'OFFEN', CURRENT_TIMESTAMP()),
(2, 'Migrationsskript', 'Datenmigration vorbereiten', 1, 5, 5, 'IN_BEARBEITUNG', CURRENT_TIMESTAMP()),
(3, 'Smoke-Test', 'Abnahmetests durchführen', 1, 4, 4, 'ERLEDIGT', CURRENT_TIMESTAMP()),
(4, 'Alerting anpassen', 'Schwellwerte neu setzen', 2, 5, 5, 'ERLEDIGT', CURRENT_TIMESTAMP()),
(5, 'Altserver dokumentieren', 'Stilllegung dokumentieren', 3, 6, 6, 'OFFEN', CURRENT_TIMESTAMP());

ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE projects ALTER COLUMN id RESTART WITH 100;
ALTER TABLE project_members ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tasks ALTER COLUMN id RESTART WITH 100;
=======
INSERT INTO users (id, username, password, email, role, active) VALUES
(1, 'admin', '{noop}admin123', 'admin@itcompany.local', 'ADMIN', true),
(2, 'leiter1', '{noop}leiter123', 'leiter1@itcompany.local', 'PROJEKTLEITER', true),
(3, 'mitarbeiter1', '{noop}mit123', 'mitarbeiter1@itcompany.local', 'MITARBEITER', true),
(4, 'mitarbeiter2', '{noop}mit123', 'mitarbeiter2@itcompany.local', 'MITARBEITER', true);

INSERT INTO projects (id, name, description, project_manager_id, status, created_at, updated_at) VALUES
(1, 'Kundenportal', 'Neues Kundenportal für Ticketing', 2, 'AKTIV', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO project_members (id, project_id, user_id, joined_at) VALUES
(1, 1, 3, CURRENT_TIMESTAMP()),
(2, 1, 4, CURRENT_TIMESTAMP());

INSERT INTO tasks (id, title, description, project_id, created_by_id, assigned_to_id, status, created_at, updated_at) VALUES
(1, 'Frontend Grundlayout', 'Dashboard und Navigation erstellen', 1, 3, 3, 'ERLEDIGT', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
(2, 'API Integration', 'Tasks über REST API anbinden', 1, 3, 4, 'IN_BEARBEITUNG', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
>>>>>>> origin/main
