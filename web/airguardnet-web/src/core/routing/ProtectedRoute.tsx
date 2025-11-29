import { Navigate } from 'react-router-dom'

export const ProtectedRoute = ({
  isAuth,
  children
}: {
  isAuth: boolean
  children: JSX.Element
}) => {
  if (!isAuth) return <Navigate to="/login" replace />
  return children
}
