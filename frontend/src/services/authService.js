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
}
