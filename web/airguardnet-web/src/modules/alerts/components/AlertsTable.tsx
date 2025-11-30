import { Table, TableBody, TableCell, TableHead, TableRow, Chip } from '@mui/material'
import type { Alert } from '../../../core/types'

const severityColor: Record<Alert['severity'], 'error' | 'warning' | 'primary'> = {
  CRITICAL: 'error',
  HIGH: 'warning',
  MEDIUM: 'primary',
}

const statusLabel: Record<Alert['status'], string> = {
  PENDING: 'Pendiente',
  IN_PROGRESS: 'En proceso',
  RESOLVED: 'Resuelta',
}

interface AlertsTableProps {
  alerts: Alert[]
  onSelect: (alert: Alert) => void
}

export const AlertsTable = ({ alerts, onSelect }: AlertsTableProps) => (
  <Table size="small">
    <TableHead>
      <TableRow>
        <TableCell>Dispositivo</TableCell>
        <TableCell>Severidad</TableCell>
        <TableCell>Estado</TableCell>
        <TableCell>Fecha</TableCell>
        <TableCell>Mensaje</TableCell>
      </TableRow>
    </TableHead>
    <TableBody>
      {alerts.map((alert) => (
        <TableRow key={alert.id} hover onClick={() => onSelect(alert)} sx={{ cursor: 'pointer' }}>
          <TableCell>{alert.deviceId}</TableCell>
          <TableCell>
            <Chip label={alert.severity} color={severityColor[alert.severity]} size="small" />
          </TableCell>
          <TableCell>
            <Chip label={statusLabel[alert.status]} variant="outlined" size="small" />
          </TableCell>
          <TableCell>{new Date(alert.createdAt).toLocaleString()}</TableCell>
          <TableCell>{alert.message}</TableCell>
        </TableRow>
      ))}
    </TableBody>
  </Table>
)
