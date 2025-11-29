import { createBrowserRouter } from 'react-router-dom'
import MainLayout from '../layout/MainLayout'
import AuthLayout from '../layout/AuthLayout'
import { LoginPage } from '../../modules/auth/pages/LoginPage'
import { DashboardPage } from '../../modules/dashboard/pages/DashboardPage'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      { path: 'dashboard', element: <DashboardPage /> },
    ],
  },
  {
    path: '/',
    element: <AuthLayout />,
    children: [
      { path: 'login', element: <LoginPage /> },
    ],
  },
])
