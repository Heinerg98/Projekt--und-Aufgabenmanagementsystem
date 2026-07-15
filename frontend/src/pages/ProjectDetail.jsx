import { useCallback, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useParams } from 'react-router-dom'
import TaskForm from '../components/TaskForm'
import TaskList from '../components/TaskList'
import { useAuth } from '../context/AuthContext'
import { getProject } from '../services/projectService'
import { createTask, getTasksByProject, updateTaskStatus } from '../services/taskService'

export default function ProjectDetail() {
  const { token } = useAuth()
  const { id } = useParams()
  const [project, setProject] = useState(null)
  const [tasks, setTasks] = useState([])
  const [error, setError] = useState('')

  const load = useCallback(async () => {
    try {
      setError('')
      const [projectData, taskData] = await Promise.all([
        getProject(token, id),
        getTasksByProject(token, id)
      ])
      setProject(projectData)
      setTasks(taskData)
    } catch {
      setError('Projektdaten konnten nicht geladen werden.')
    }
  }, [id, token])

  useEffect(() => {
    load()
  }, [load])

  const handleCreateTask = async (payload) => {
    await createTask(token, id, payload)
    await load()
  }

  const handleStatusChange = async (taskId, status) => {
    await updateTaskStatus(token, taskId, status)
    await load()
  }

  if (!project) {
    return <div className="container">Projekt wird geladen...</div>
  }

  return (
    <div className="container stack">
      <h2>{project.name}</h2>
      <p>{project.description}</p>
      <p>Fortschritt: {project.progress}%</p>
      <Link to={`/projects/${project.id}/members`}>Mitglieder verwalten</Link>
      {error && <p className="error">{error}</p>}
      <TaskForm onSubmit={handleCreateTask} />
      <TaskList tasks={tasks} onStatusChange={handleStatusChange} />
    </div>
  )
}
