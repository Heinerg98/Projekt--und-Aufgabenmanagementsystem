import { apiRequest } from './api'

export const getTasksByProject = (token, projectId) => apiRequest(`/projects/${projectId}/tasks`, {}, token)
export const createTask = (token, projectId, payload) => apiRequest(`/projects/${projectId}/tasks`, {
  method: 'POST',
  body: JSON.stringify(payload)
}, token)
export const updateTask = (token, taskId, payload) => apiRequest(`/tasks/${taskId}`, {
  method: 'PUT',
  body: JSON.stringify(payload)
}, token)
export const updateTaskStatus = (token, taskId, status) => apiRequest(`/tasks/${taskId}/status`, {
  method: 'PATCH',
  body: JSON.stringify({ status })
}, token)
