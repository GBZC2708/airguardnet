import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { authApi } from '../../../core/api/authApi'
import type { LoginPayload } from '../../../core/api/authApi'
import type { User } from '../../../core/types'

interface AuthContextValue {
  user: User | null
  token: string | null
  loading: boolean
  login: (credentials: LoginPayload) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const navigate = useNavigate()
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    const storedToken = localStorage.getItem('airguardnet_token')
    const storedUser = localStorage.getItem('airguardnet_user')
    if (storedToken && storedUser) {
      setToken(storedToken)
      try {
        setUser(JSON.parse(storedUser))
      } catch (err) {
        localStorage.removeItem('airguardnet_user')
      }
    }
  }, [])

  const login = async (credentials: LoginPayload) => {
    setLoading(true)
    try {
      const response = await authApi.login(credentials)
      if (!response.success || !response.data) {
        throw new Error(response.message || 'Error al iniciar sesión')
      }
      const loggedUser: User = {
        id: response.data.userId,
        name: response.data.name,
        lastName: '',
        email: response.data.email,
        role: response.data.role,
        planId: response.data.planId,
        status: 'ACTIVE'
      }
      setUser(loggedUser)
      setToken(response.data.token)
      localStorage.setItem('airguardnet_token', response.data.token)
      localStorage.setItem('airguardnet_user', JSON.stringify(loggedUser))
      navigate('/dashboard')
    } finally {
      setLoading(false)
    }
  }

  const logout = () => {
    setUser(null)
    setToken(null)
    localStorage.removeItem('airguardnet_token')
    localStorage.removeItem('airguardnet_user')
    navigate('/login')
  }

  const value = useMemo(
    () => ({ user, token, login, logout, loading }),
    [user, token, loading]
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export const useAuthContext = () => {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuthContext must be used within AuthProvider')
  return context
}
