import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { addMember, getMembers } from '../services/projectService'

export default function ManageMembers() {
  const { token } = useAuth()
  const { id } = useParams()
  const [members, setMembers] = useState([])
  const [userId, setUserId] = useState('')

  const load = async () => {
    const data = await getMembers(token, id)
    setMembers(data)
  }

  useEffect(() => {
    load().catch(() => setMembers([]))
  }, [id, token])

  const submit = async (e) => {
    e.preventDefault()
    await addMember(token, id, Number(userId))
    setUserId('')
    await load()
  }

  return (
    <div className="container stack">
      <h2>Projektmitglieder verwalten</h2>
      <form onSubmit={submit} className="stack">
        <input value={userId} onChange={(e) => setUserId(e.target.value)} type="number" min="1" placeholder="User-ID" required />
        <button type="submit">Hinzufügen</button>
      </form>
      {members.map((member) => (
        <div key={member.id} className="card">{member.username} ({member.role})</div>
      ))}
    </div>
  )
}
