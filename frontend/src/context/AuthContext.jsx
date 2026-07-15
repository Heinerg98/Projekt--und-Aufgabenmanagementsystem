import { createContext, useContext, useEffect, useState } from 'react'
import { getCurrentUser, login as apiLogin, logout as apiLogout } from '../services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'))
  const [user, setUser] = useState(null)

  useEffect(() => {
    if (!token) {
      setUser(null)
      return
    }
    getCurrentUser(token)
      .then(setUser)
      .catch(() => {
        setToken(null)
        localStorage.removeItem('token')
      })
  }, [token])

  const login = async (username, password) => {
    const response = await apiLogin(username, password)
    setToken(response.token)
    localStorage.setItem('token', response.token)
    setUser(response.user)
  }

  const logout = async () => {
    await apiLogout(token)
    setToken(null)
    setUser(null)
    localStorage.removeItem('token')
  }

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
