import { Table, TableBody, TableCell, TableHead, TableRow, Chip, Button, Typography, Stack, Tooltip } from '@mui/material'
import type { User } from '../../../core/types'

interface Props {
  users: User[]
  planById: Record<number, string>
  isAdmin: boolean
}

export const UsersTable = ({ users, planById, isAdmin }: Props) => (
  <Table size="small">
    <TableHead>
      <TableRow>
        <TableCell>Nombre</TableCell>
        <TableCell>Correo</TableCell>
        <TableCell>Rol</TableCell>
        <TableCell>Plan</TableCell>
        <TableCell>Estado</TableCell>
        <TableCell>Último acceso</TableCell>
        <TableCell align="right">Acciones</TableCell>
      </TableRow>
    </TableHead>
    <TableBody>
      {users.length === 0 ? (
        <TableRow>
          <TableCell colSpan={7}>
            <Stack alignItems="center" py={2} spacing={1}>
              <Typography variant="body2" color="text.secondary">
                No hay usuarios registrados.
              </Typography>
            </Stack>
          </TableCell>
        </TableRow>
      ) : (
        users.map((user) => (
          <TableRow key={user.id}>
            <TableCell>
              {user.name} {user.lastName}
            </TableCell>
            <TableCell>{user.email}</TableCell>
            <TableCell>{user.role}</TableCell>
            <TableCell>{user.planId ? planById[user.planId] || 'N/D' : 'N/D'}</TableCell>
            <TableCell>
              <Chip label={user.status === 'ACTIVE' ? 'Activo' : 'Deshabilitado'} color={user.status === 'ACTIVE' ? 'success' : 'default'} size="small" />
            </TableCell>
            <TableCell>{user.lastLoginAt ? new Date(user.lastLoginAt).toLocaleString() : 'N/D'}</TableCell>
            <TableCell align="right">
              {isAdmin ? (
                <Stack direction="row" spacing={1} justifyContent="flex-end">
                  <Tooltip title="La actualización de estado no está disponible en la versión actual del backend">
                    <span>
                      <Button size="small" variant="outlined" disabled>
                        {user.status === 'ACTIVE' ? 'Desactivar' : 'Activar'}
                      </Button>
                    </span>
                  </Tooltip>
                  <Tooltip title="Eliminar usuarios requiere soporte del backend. Desactiva al usuario mientras tanto.">
                    <span>
                      <Button size="small" color="error" variant="outlined" disabled>
                        Eliminar
                      </Button>
                    </span>
                  </Tooltip>
                </Stack>
              ) : (
                <Typography variant="body2" color="text.secondary">
                  Acciones solo para ADMIN
                </Typography>
              )}
            </TableCell>
          </TableRow>
        ))
      )}
    </TableBody>
  </Table>
)
