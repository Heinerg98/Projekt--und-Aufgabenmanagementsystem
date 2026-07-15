import { useState } from 'react'

export default function UserForm({ onSubmit }) {
  const [form, setForm] = useState({ username: '', password: '', email: '', role: 'MITARBEITER', active: true })

  const submit = async (e) => {
    e.preventDefault()
    await onSubmit(form)
    setForm({ username: '', password: '', email: '', role: 'MITARBEITER', active: true })
  }

  return (
    <form onSubmit={submit} className="stack">
      <input placeholder="Username" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} required />
      <input placeholder="E-Mail" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
      <input placeholder="Passwort" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
      <select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}>
        <option value="ADMIN">ADMIN</option>
        <option value="PROJEKTLEITER">PROJEKTLEITER</option>
        <option value="MITARBEITER">MITARBEITER</option>
      </select>
      <button type="submit">Benutzer speichern</button>
    </form>
  )
}
