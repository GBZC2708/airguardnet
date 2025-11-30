import type { ReactElement } from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../../modules/auth/hooks/useAuth'

export const ProtectedRoute = ({ children }: { children: ReactElement }) => {
  const { token, loading } = useAuth()

  if (loading) return null
  if (!token) return <Navigate to="/login" replace />
  return children
}
