import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { loginRequest } from '../services/authService';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault();
    try {
      const response = await loginRequest(username, password);
      login(response);
      navigate('/dashboard');
    } catch (e) {
      setError('Ungültige Zugangsdaten');
    }
  };

  return (
    <main>
      <h1>Login</h1>
      <form onSubmit={submit}>
        <label htmlFor="username">Benutzername</label>
        <input id="username" value={username} onChange={(e) => setUsername(e.target.value)} />
        <label htmlFor="password">Passwort</label>
        <input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        <button type="submit">Anmelden</button>
      </form>
      {error && <p>{error}</p>}
    </main>
  );
}
