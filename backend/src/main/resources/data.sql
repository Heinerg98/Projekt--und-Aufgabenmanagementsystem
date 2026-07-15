INSERT INTO users (id, username, password, email, role, active) VALUES
  (1, 'admin', '{bcrypt}$2b$10$FzoeJp6afd4HMt7kqcPB0ubz6i7cigsT6onzVewP/SaLkaqrc1Fs.', 'admin@itcompany.com', 'ADMIN', true),
  (2, 'leiter1', '{bcrypt}$2b$10$uhn6jnppWXXILKRziUIfJ.Hfs6yO44I9lphbKpKkp4l3aes0w0Fv6', 'leiter1@itcompany.com', 'PROJEKTLEITER', true),
  (3, 'leiter2', '{bcrypt}$2b$10$jK3Id6MZvbDdteHr92pLzu.wLND8WtICPDLSV0FotQ6QIlYNfbwpK', 'leiter2@itcompany.com', 'PROJEKTLEITER', true),
  (4, 'mitarbeiter1', '{bcrypt}$2b$10$oarIunYEXW/bH3qPFDBrQ.IZe4oh9fnq4HjdcV8Aysn9fH7tpdKGO', 'mit1@itcompany.com', 'MITARBEITER', true),
  (5, 'mitarbeiter2', '{bcrypt}$2b$10$YgMBH4z1WKdis6uDIKJ2HeQQQWifQaeRB1HKwxVHXrRByxQTsmFUy', 'mit2@itcompany.com', 'MITARBEITER', true),
  (6, 'mitarbeiter3', '{bcrypt}$2b$10$8dW1asG.FKCn0zzn4uvnoeOB1GCz5wjPwvpsoel54TaCsXZ6HS2Yy', 'mit3@itcompany.com', 'MITARBEITER', true);

INSERT INTO projects (id, name, description, project_manager_id, status, created_at, updated_at) VALUES
  (1, 'Website Redesign', 'Modernisierung der Unternehmenswebsite', 2, 'AKTIV', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (2, 'Mobile App Dev', 'Entwicklung einer neuen mobilen Anwendung', 2, 'AKTIV', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (3, 'Legacy System Migration', 'Migration des alten Systems', 3, 'AKTIV', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (4, 'Archiviertes Projekt', 'Ein bereits abgeschlossenes Projekt', 2, 'ARCHIVIERT', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO project_members (id, project_id, user_id, joined_at) VALUES
  (1, 1, 4, CURRENT_TIMESTAMP()),
  (2, 1, 5, CURRENT_TIMESTAMP()),
  (3, 2, 4, CURRENT_TIMESTAMP()),
  (4, 3, 6, CURRENT_TIMESTAMP());

INSERT INTO tasks (id, title, description, project_id, created_by_id, assigned_to_id, status, created_at, updated_at) VALUES
  (1, 'Homepage erstellen', 'Designen und implementieren der Homepage', 1, 2, 4, 'ERLEDIGT', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (2, 'CSS-Styling', 'Komplettes Styling durchführen', 1, 2, 5, 'IN_BEARBEITUNG', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (3, 'Testing durchführen', 'QA und Testing der Website', 1, 2, 4, 'OFFEN', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (4, 'API-Design', 'REST-API für Mobile App entwerfen', 2, 2, 4, 'IN_BEARBEITUNG', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (5, 'Backend-Implementierung', 'Server-Logik implementieren', 2, 2, NULL, 'OFFEN', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (6, 'Datenmigration', 'Alte Daten ins neue System migrieren', 3, 3, 6, 'OFFEN', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

ALTER TABLE users ALTER COLUMN id RESTART WITH 7;
ALTER TABLE projects ALTER COLUMN id RESTART WITH 5;
ALTER TABLE project_members ALTER COLUMN id RESTART WITH 5;
ALTER TABLE tasks ALTER COLUMN id RESTART WITH 7;
