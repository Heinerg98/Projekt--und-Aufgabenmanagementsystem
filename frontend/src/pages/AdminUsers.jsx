import { useEffect, useState } from 'react'
import UserForm from '../components/UserForm'
import { useAuth } from '../context/AuthContext'
import { createUser, getUsers } from '../services/userService'

export default function AdminUsers() {
  const { token } = useAuth()
  const [users, setUsers] = useState([])
  const [error, setError] = useState('')

  const load = async () => {
    try {
      setError('')
      const data = await getUsers(token)
      setUsers(data)
    } catch (e) {
      setUsers([])
      setError(e.message)
    }
  }

  useEffect(() => {
    load()
  }, [token])

  const handleCreate = async (payload) => {
    await createUser(token, payload)
    await load()
  }

  return (
    <div className="container stack">
      <h2>Benutzerverwaltung</h2>
      <UserForm onSubmit={handleCreate} />
      {error && <p className="error">{error}</p>}
      {users.map((user) => (
        <div key={user.id} className="card">
          {user.username} - {user.role} - {user.email}
        </div>
      ))}
    </div>
  )
}
