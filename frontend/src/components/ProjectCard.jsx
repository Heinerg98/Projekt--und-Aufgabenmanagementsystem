import { Link } from 'react-router-dom'

export default function ProjectCard({ project }) {
  return (
    <div className="card">
      <h3>{project.name}</h3>
      <p>{project.description}</p>
      <p>Status: {project.status}</p>
      <p>Fortschritt: {project.progress}% ({project.completedTasks}/{project.totalTasks})</p>
      <Link to={`/projects/${project.id}`}>Details</Link>
    </div>
  )
}
