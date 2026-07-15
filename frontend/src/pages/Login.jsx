import { useState } from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const { user, login } = useAuth()
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('admin123')
  const [error, setError] = useState('')

  if (user) {
    return <Navigate to="/dashboard" replace />
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    try {
      await login(username, password)
    } catch {
      setError('Login fehlgeschlagen')
    }
  }

  return (
    <div className="container">
      <h1>Projekt- und Aufgabenmanagement</h1>
      <form onSubmit={handleSubmit} className="stack">
        <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Username" required />
        <input value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Passwort" type="password" required />
        <button type="submit">Login</button>
      </form>
      {error && <p className="error">{error}</p>}
      <p>Testuser: admin/admin123, leiter1/leiter123, mitarbeiter1/mit123</p>
    </div>
  )
}
