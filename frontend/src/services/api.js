const API_BASE = 'http://localhost:8080/api'

export async function apiRequest(path, options = {}, token) {
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  }

  if (token) {
    headers['X-Auth-Token'] = token
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers
  })

  if (!response.ok) {
    const contentType = response.headers.get('content-type') || ''
    if (contentType.includes('application/json')) {
      const json = await response.json()
      throw new Error(json.error || 'Request fehlgeschlagen')
    }

    const body = await response.text()
    throw new Error(body || 'Request fehlgeschlagen')
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}
