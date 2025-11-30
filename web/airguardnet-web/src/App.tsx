import { Routes, Route, Navigate } from 'react-router-dom'
import { AuthLayout } from './core/layout/AuthLayout'
import { MainLayout } from './core/layout/MainLayout'
import { LoginPage } from './modules/auth/pages/LoginPage'
import { RegisterPage } from './modules/auth/pages/RegisterPage'
import { ForgotPasswordPage } from './modules/auth/pages/ForgotPasswordPage'
import { DashboardPage } from './modules/dashboard/pages/DashboardPage'
import { DevicesListPage } from './modules/devices/pages/DevicesListPage'
import { DeviceDetailPage } from './modules/devices/pages/DeviceDetailPage'
import { AlertsCenterPage } from './modules/alerts/pages/AlertsCenterPage'
import { ReportsPage } from './modules/reports/pages/ReportsPage'
import { UsersManagementPage } from './modules/users/pages/UsersManagementPage'
import { SystemConfigPage } from './modules/config/pages/SystemConfigPage'
import { SensorThresholdsPage } from './modules/config/pages/SensorThresholdsPage'
import { ConfigChangeLogPage } from './modules/config/pages/ConfigChangeLogPage'
import { DocumentationPage } from './modules/docs/pages/DocumentationPage'
import { VersionHistoryPage } from './modules/docs/pages/VersionHistoryPage'
import { SystemLogsPage } from './modules/logs/pages/SystemLogsPage'
import { AccessLogsPage } from './modules/logs/pages/AccessLogsPage'
import { SimulationPage } from './modules/simulation/pages/SimulationPage'
import { ProtectedRoute } from './core/routing/ProtectedRoute'

const App = () => {
  return (
    <Routes>
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
      </Route>

      <Route
        path="/"
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="dashboard" element={<DashboardPage />} />
        <Route path="devices" element={<DevicesListPage />} />
        <Route path="devices/:id" element={<DeviceDetailPage />} />
        <Route path="alerts" element={<AlertsCenterPage />} />
        <Route path="reports" element={<ReportsPage />} />
        <Route path="users" element={<UsersManagementPage />} />
        <Route path="config/system" element={<SystemConfigPage />} />
        <Route path="config/sensors" element={<SensorThresholdsPage />} />
        <Route path="config/logs" element={<ConfigChangeLogPage />} />
        <Route path="docs" element={<DocumentationPage />} />
        <Route path="docs/versions" element={<VersionHistoryPage />} />
        <Route path="logs/system" element={<SystemLogsPage />} />
        <Route path="logs/access" element={<AccessLogsPage />} />
        <Route path="simulation" element={<SimulationPage />} />
      </Route>
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  )
}

export default App
