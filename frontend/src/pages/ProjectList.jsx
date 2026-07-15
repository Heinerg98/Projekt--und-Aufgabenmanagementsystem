import { useEffect, useState } from 'react'
import ProjectCard from '../components/ProjectCard'
import { useAuth } from '../context/AuthContext'
import { getProjects } from '../services/projectService'

export default function ProjectList() {
  const { token } = useAuth()
  const [projects, setProjects] = useState([])

  useEffect(() => {
    getProjects(token).then(setProjects).catch(() => setProjects([]))
  }, [token])

  return (
    <div className="container">
      <h2>Projektliste</h2>
      <div className="grid">
        {projects.map((project) => <ProjectCard key={project.id} project={project} />)}
      </div>
    </div>
  )
}
