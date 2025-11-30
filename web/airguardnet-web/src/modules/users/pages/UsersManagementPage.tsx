import { useEffect, useMemo, useState } from 'react'
import { Alert, Button, LinearProgress, Stack, Typography } from '@mui/material'
import type { Plan, User } from '../../../core/types'
import { userApi } from '../../../core/api/userApi'
import { UsersTable } from '../components/UsersTable'
import { UserFormDialog } from '../components/UserFormDialog'
import { useAuth } from '../../auth/hooks/useAuth'

export const UsersManagementPage = () => {
  const [users, setUsers] = useState<User[]>([])
  const [plans, setPlans] = useState<Plan[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [open, setOpen] = useState(false)
  const { user } = useAuth()
  const isAdmin = user?.role === 'ADMIN'

  useEffect(() => {
    const load = async () => {
      setLoading(true)
      setError(null)
      try {
        const [usersResponse, plansResponse] = await Promise.all([userApi.getUsers(), userApi.getPlans()])
        setUsers(usersResponse)
        setPlans(plansResponse)
      } catch (err) {
        const message = err instanceof Error ? err.message : 'No se pudieron cargar los usuarios'
        setError(message)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  const planById = useMemo(
    () => plans.reduce<Record<number, string>>((acc, plan) => ({ ...acc, [plan.id]: plan.name }), {}),
    [plans]
  )

  const handleUserCreated = (user: User) => {
    setUsers((prev) => [user, ...prev])
    setOpen(false)
  }

  if (loading) return <LinearProgress />

  return (
    <Stack spacing={2}>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography variant="h5" fontWeight={800}>
          Usuarios
        </Typography>
        {isAdmin && (
          <Button
            variant="contained"
            onClick={() => setOpen(true)}
            sx={{ transition: 'transform 160ms ease', '&:hover': { transform: 'translateY(-1px)' } }}
          >
            Nuevo usuario
          </Button>
        )}
      </Stack>
      {error && <Alert severity="error">{error}</Alert>}
      {!isAdmin && (
        <Alert severity="info">
          Solo los administradores pueden crear, activar o eliminar usuarios. Puedes consultar la información en modo solo lectura.
        </Alert>
      )}
      <UsersTable users={users} planById={planById} isAdmin={isAdmin} />
      {isAdmin && (
        <UserFormDialog open={open} onClose={() => setOpen(false)} plans={plans} onUserCreated={handleUserCreated} />
      )}
    </Stack>
  )
}
