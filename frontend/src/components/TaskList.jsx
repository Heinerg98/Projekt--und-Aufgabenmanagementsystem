export default function TaskList({ tasks, onStatusChange }) {
  return (
    <div className="stack">
      {tasks.map((task) => (
        <div key={task.id} className="card">
          <h4>{task.title}</h4>
          <p>{task.description}</p>
          <p>Status: {task.status}</p>
          <select value={task.status} onChange={(e) => onStatusChange(task.id, e.target.value)}>
            <option value="OFFEN">OFFEN</option>
            <option value="IN_BEARBEITUNG">IN_BEARBEITUNG</option>
            <option value="ERLEDIGT">ERLEDIGT</option>
          </select>
        </div>
      ))}
    </div>
  )
}
