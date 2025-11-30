import { useEffect, useState } from 'react'
import { Button, LinearProgress, Stack, Typography } from '@mui/material'
import httpClient from '../../../core/api/httpClient'
import type { ApiResponse, User } from '../../../core/types'
import { UsersTable } from '../components/UsersTable'
import { UserFormDialog } from '../components/UserFormDialog'

export const UsersManagementPage = () => {
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)
  const [open, setOpen] = useState(false)

  useEffect(() => {
    const load = async () => {
      const { data } = await httpClient.get<ApiResponse<User[]>>('/users')
      setUsers(data.data || [])
      setLoading(false)
    }
    load()
  }, [])

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
      <UsersTable users={users} />
      <UserFormDialog open={open} onClose={() => setOpen(false)} />
    </Stack>
  )
}
