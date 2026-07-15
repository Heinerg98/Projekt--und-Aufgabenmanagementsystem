import { useMemo } from 'react';
import { useAuth } from '../context/AuthContext';
import { filterProjectsForUser } from '../services/projectVisibility';

const projects = [
  { id: 1, name: 'CRM-Migration', projectLeadId: 2, memberIds: [4, 5] },
  { id: 2, name: 'Monitoring-Optimierung', projectLeadId: 2, memberIds: [5] },
  { id: 3, name: 'Legacy-Abbau', projectLeadId: 3, memberIds: [6] },
];

const users = {
  admin: { id: 1, role: 'ADMIN' },
  leiter1: { id: 2, role: 'PROJEKTLEITER' },
  mitarbeiter1: { id: 4, role: 'MITARBEITER' },
};

export default function DashboardPage() {
  const { username, role } = useAuth();
  const currentUser = useMemo(() => users[username] || null, [username]);
  const visible = currentUser ? filterProjectsForUser(projects, currentUser) : [];

  return (
    <main>
      <h1>Dashboard</h1>
      <p>Rolle: {currentUser?.role ?? role}</p>
      <ul>
        {visible.map((project) => (
          <li key={project.id}>{project.name}</li>
        ))}
      </ul>
      {role === 'PROJEKTLEITER' && <button>Projekt erstellen</button>}
      {role === 'ADMIN' && <button>Benutzer verwalten</button>}
    </main>
  );
}
