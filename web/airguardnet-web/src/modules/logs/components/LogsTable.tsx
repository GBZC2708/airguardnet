import { Chip, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material'
import type { AccessLog, SystemLog } from '../../../core/types'

interface LogsTableProps {
  variant: 'access' | 'system'
  data: AccessLog[] | SystemLog[]
}

const systemColors: Record<SystemLog['type'], 'error' | 'warning' | 'info'> = {
  ERROR: 'error',
  WARNING: 'warning',
  INFO: 'info'
}

export const LogsTable = ({ variant, data }: LogsTableProps) => {
  return (
    <Table size="small">
      <TableHead>
        <TableRow>
          <TableCell>ID</TableCell>
          {variant === 'access' ? (
            <>
              <TableCell>Usuario</TableCell>
              <TableCell>Acción</TableCell>
              <TableCell>IP</TableCell>
            </>
          ) : (
            <>
              <TableCell>Tipo</TableCell>
              <TableCell>Origen</TableCell>
              <TableCell>Mensaje</TableCell>
            </>
          )}
          <TableCell>Fecha</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {variant === 'access'
          ? (data as AccessLog[]).map((log) => (
              <TableRow key={log.id}>
                <TableCell>{log.id}</TableCell>
                <TableCell>{log.userId}</TableCell>
                <TableCell>{log.action}</TableCell>
                <TableCell>{log.ipAddress}</TableCell>
                <TableCell>{new Date(log.createdAt).toLocaleString()}</TableCell>
              </TableRow>
            ))
          : (data as SystemLog[]).map((log) => (
              <TableRow key={log.id}>
                <TableCell>{log.id}</TableCell>
                <TableCell>
                  <Chip label={log.type} color={systemColors[log.type]} size="small" />
                </TableCell>
                <TableCell>{log.source}</TableCell>
                <TableCell>{log.message}</TableCell>
                <TableCell>{new Date(log.createdAt).toLocaleString()}</TableCell>
              </TableRow>
            ))}
      </TableBody>
    </Table>
  )
}
