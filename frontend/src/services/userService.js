import { apiRequest } from './api'

export const getUsers = (token) => apiRequest('/users', {}, token)
export const createUser = (token, payload) => apiRequest('/users', {
  method: 'POST',
  body: JSON.stringify(payload)
}, token)
export const updateUser = (token, id, payload) => apiRequest(`/users/${id}`, {
  method: 'PUT',
  body: JSON.stringify(payload)
}, token)
