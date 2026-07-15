import { Navigate, Route, Routes } from 'react-router-dom'
import Navigation from './components/Navigation'
import { useAuth } from './context/AuthContext'
import AdminUsers from './pages/AdminUsers'
import CreateProject from './pages/CreateProject'
import Dashboard from './pages/Dashboard'
import Login from './pages/Login'
import ManageMembers from './pages/ManageMembers'
import Profile from './pages/Profile'
import ProjectDetail from './pages/ProjectDetail'
import ProjectList from './pages/ProjectList'

function ProtectedRoute({ children, roles }) {
  const { user } = useAuth()

  if (!user) {
    return <Navigate to="/login" replace />
  }

  if (roles && !roles.includes(user.role)) {
    return <Navigate to="/dashboard" replace />
  }

  return (
    <>
      <Navigation />
      {children}
    </>
  )
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
      <Route path="/projects" element={<ProtectedRoute><ProjectList /></ProtectedRoute>} />
      <Route path="/projects/new" element={<ProtectedRoute roles={['PROJEKTLEITER']}><CreateProject /></ProtectedRoute>} />
      <Route path="/projects/:id" element={<ProtectedRoute><ProjectDetail /></ProtectedRoute>} />
      <Route path="/projects/:id/members" element={<ProtectedRoute roles={['PROJEKTLEITER']}><ManageMembers /></ProtectedRoute>} />
      <Route path="/admin/users" element={<ProtectedRoute roles={['ADMIN']}><AdminUsers /></ProtectedRoute>} />
      <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  )
}
