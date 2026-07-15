import { createContext, useContext, useMemo, useState } from 'react';

const AuthContext = createContext(null);
const TOKEN_KEY = 'pms_token';
const USERNAME_KEY = 'pms_username';
const ROLE_KEY = 'pms_role';

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY));
  const [username, setUsername] = useState(() => localStorage.getItem(USERNAME_KEY));
  const [role, setRole] = useState(() => localStorage.getItem(ROLE_KEY));

  const login = ({ token: nextToken, username: nextUsername, role: nextRole }) => {
    localStorage.setItem(TOKEN_KEY, nextToken);
    localStorage.setItem(USERNAME_KEY, nextUsername);
    localStorage.setItem(ROLE_KEY, nextRole);
    setToken(nextToken);
    setUsername(nextUsername);
    setRole(nextRole);
  };

  const logout = () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USERNAME_KEY);
    localStorage.removeItem(ROLE_KEY);
    setToken(null);
    setUsername(null);
    setRole(null);
  };

  const value = useMemo(() => ({
    token,
    username,
    role,
    isAuthenticated: Boolean(token),
    login,
    logout,
  }), [token, username, role]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth muss innerhalb von AuthProvider verwendet werden');
  }
  return context;
}
