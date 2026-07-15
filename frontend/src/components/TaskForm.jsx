import { useState } from 'react'

export default function TaskForm({ onSubmit }) {
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')

  const handleSubmit = async (event) => {
    event.preventDefault()
    await onSubmit({ title, description, status: 'OFFEN' })
    setTitle('')
    setDescription('')
  }

  return (
    <form onSubmit={handleSubmit} className="stack">
      <input placeholder="Titel" value={title} onChange={(e) => setTitle(e.target.value)} required />
      <textarea placeholder="Beschreibung" value={description} onChange={(e) => setDescription(e.target.value)} />
      <button type="submit">Aufgabe speichern</button>
    </form>
  )
}
