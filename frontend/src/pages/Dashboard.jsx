import { useEffect, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { getProjects } from '../services/projectService'

export default function Dashboard() {
  const { token, user } = useAuth()
  const [projects, setProjects] = useState([])

  useEffect(() => {
    getProjects(token).then(setProjects).catch(() => setProjects([]))
  }, [token])

  const completed = projects.reduce((sum, p) => sum + p.completedTasks, 0)
  const total = projects.reduce((sum, p) => sum + p.totalTasks, 0)

  return (
    <div className="container">
      <h2>Dashboard</h2>
      <p>Willkommen, {user?.username}</p>
      <p>Sichtbare Projekte: {projects.length}</p>
      <p>Aufgaben erledigt: {completed}/{total}</p>
    </div>
  )
}
