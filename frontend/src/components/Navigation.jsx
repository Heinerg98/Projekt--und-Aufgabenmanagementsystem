import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navigation() {
  const { user, logout } = useAuth()

  return (
    <nav className="nav">
      <Link to="/dashboard">Dashboard</Link>
      <Link to="/projects">Projekte</Link>
      {user?.role === 'PROJEKTLEITER' && <Link to="/projects/new">Projekt erstellen</Link>}
      {user?.role === 'ADMIN' && <Link to="/admin/users">Benutzer</Link>}
      <Link to="/profile">Profil</Link>
      <button onClick={logout}>Logout</button>
    </nav>
  )
}
