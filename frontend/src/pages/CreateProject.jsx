import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { createProject } from '../services/projectService'

export default function CreateProject() {
  const { token } = useAuth()
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')

  const submit = async (e) => {
    e.preventDefault()
    const project = await createProject(token, { name, description })
    navigate(`/projects/${project.id}`)
  }

  return (
    <div className="container">
      <h2>Projekt erstellen</h2>
      <form onSubmit={submit} className="stack">
        <input value={name} onChange={(e) => setName(e.target.value)} placeholder="Projektname" required />
        <textarea value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Beschreibung" />
        <button type="submit">Anlegen</button>
      </form>
    </div>
  )
}
