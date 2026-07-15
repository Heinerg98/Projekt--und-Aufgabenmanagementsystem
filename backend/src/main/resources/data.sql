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
