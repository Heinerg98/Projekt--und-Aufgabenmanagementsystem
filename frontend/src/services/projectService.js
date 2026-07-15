import { apiRequest } from './api'

export const getProjects = (token) => apiRequest('/projects', {}, token)
export const getProject = (token, id) => apiRequest(`/projects/${id}`, {}, token)
export const createProject = (token, payload) => apiRequest('/projects', {
  method: 'POST',
  body: JSON.stringify(payload)
}, token)
export const updateProject = (token, id, payload) => apiRequest(`/projects/${id}`, {
  method: 'PUT',
  body: JSON.stringify(payload)
}, token)
export const archiveProject = (token, id) => apiRequest(`/projects/${id}/archive`, { method: 'POST' }, token)
export const getMembers = (token, id) => apiRequest(`/projects/${id}/members`, {}, token)
export const addMember = (token, id, userId) => apiRequest(`/projects/${id}/members`, {
  method: 'POST',
  body: JSON.stringify({ userId })
}, token)
