import { Routes, Route } from 'react-router-dom'
import { AuthLayout } from './core/layout/AuthLayout'
import { MainLayout } from './core/layout/MainLayout'
import { LoginPage } from './modules/auth/pages/LoginPage'
import { DashboardPage } from './modules/dashboard/pages/DashboardPage'

const App = () => {
  return (
    <Routes>
      {/* Rutas públicas (auth) */}
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<LoginPage />} />
      </Route>

      {/* Rutas privadas (por ahora sin auth real, solo layout) */}
      <Route element={<MainLayout />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/" element={<DashboardPage />} />
      </Route>
    </Routes>
  )
}

export default App
