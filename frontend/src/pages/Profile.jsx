import { useAuth } from '../context/AuthContext'

export default function Profile() {
  const { user } = useAuth()

  return (
    <div className="container">
      <h2>Profil</h2>
      <p>Username: {user?.username}</p>
      <p>E-Mail: {user?.email}</p>
      <p>Rolle: {user?.role}</p>
    </div>
  )
}
