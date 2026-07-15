<<<<<<< HEAD
export async function loginRequest(username, password) {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw new Error('Ungültige Zugangsdaten');
  }

  return response.json();
=======
import { apiRequest } from './api'

export function login(username, password) {
  return apiRequest('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ username, password })
  })
}

export function logout(token) {
  return apiRequest('/auth/logout', { method: 'POST' }, token)
}

export function getCurrentUser(token) {
  return apiRequest('/auth/me', {}, token)
>>>>>>> origin/main
}
