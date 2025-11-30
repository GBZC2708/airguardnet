import { Table, TableBody, TableCell, TableHead, TableRow, Chip } from '@mui/material'
import type { User } from '../../../core/types'

interface Props {
  users: User[]
}

export const UsersTable = ({ users }: Props) => (
  <Table size="small">
    <TableHead>
      <TableRow>
        <TableCell>Nombre</TableCell>
        <TableCell>Correo</TableCell>
        <TableCell>Rol</TableCell>
        <TableCell>Estado</TableCell>
        <TableCell>Último acceso</TableCell>
      </TableRow>
    </TableHead>
    <TableBody>
      {users.map((user) => (
        <TableRow key={user.id}>
          <TableCell>
            {user.name} {user.lastName}
          </TableCell>
          <TableCell>{user.email}</TableCell>
          <TableCell>{user.role}</TableCell>
          <TableCell>
            <Chip label={user.status === 'ACTIVE' ? 'Activo' : 'Deshabilitado'} color={user.status === 'ACTIVE' ? 'success' : 'default'} size="small" />
          </TableCell>
          <TableCell>{user.lastLoginAt ? new Date(user.lastLoginAt).toLocaleString() : 'N/D'}</TableCell>
        </TableRow>
      ))}
    </TableBody>
  </Table>
)
