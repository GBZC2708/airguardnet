import { useEffect, useMemo, useState } from 'react'
import { Alert, Button, LinearProgress, Stack, Typography } from '@mui/material'
import type { Plan, User } from '../../../core/types'
import { userApi } from '../../../core/api/userApi'
import { UsersTable } from '../components/UsersTable'
import { UserFormDialog } from '../components/UserFormDialog'

export const UsersManagementPage = () => {
  const [users, setUsers] = useState<User[]>([])
  const [plans, setPlans] = useState<Plan[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [open, setOpen] = useState(false)

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

  const handleStatusChange = async (userId: number, currentStatus: User['status']) => {
    try {
      const updated = await userApi.updateUserStatus(userId, currentStatus === 'ACTIVE' ? 'DISABLED' : 'ACTIVE')
      setUsers((prev) => prev.map((user) => (user.id === userId ? { ...user, status: updated.status } : user)))
    } catch (err) {
      const message = err instanceof Error ? err.message : 'No se pudo actualizar el estado'
      setError(message)
    }
  }

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
        <Button variant="contained" onClick={() => setOpen(true)}>
          Nuevo usuario
        </Button>
      </Stack>
      {error && <Alert severity="error">{error}</Alert>}
      <UsersTable users={users} planById={planById} onStatusChange={handleStatusChange} />
      <UserFormDialog open={open} onClose={() => setOpen(false)} plans={plans} onUserCreated={handleUserCreated} />
    </Stack>
  )
}
