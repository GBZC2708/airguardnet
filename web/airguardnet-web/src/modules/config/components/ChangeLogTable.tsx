import { useEffect, useState } from 'react'
import { Alert, LinearProgress, Table, TableBody, TableCell, TableHead, TableRow, Typography } from '@mui/material'
import { configApi } from '../../../core/api/configApi'
import type { ConfigChangeLog } from '../../../core/types'

export const ChangeLogTable = () => {
  const [logs, setLogs] = useState<ConfigChangeLog[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const load = async () => {
      setError(null)
      try {
        const data = await configApi.getConfigChangeLogs()
        setLogs(data || [])
      } catch (err) {
        const message = err instanceof Error ? err.message : 'No se pudo obtener el historial de cambios.'
        setError(message)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  if (loading) return <LinearProgress />

  if (error) return <Alert severity="error">{error}</Alert>

  return (
    <Table size="small">
      <TableHead>
        <TableRow>
          <TableCell>Parámetro</TableCell>
          <TableCell>Valor anterior</TableCell>
          <TableCell>Nuevo valor</TableCell>
          <TableCell>Cambiado por</TableCell>
          <TableCell>Fecha</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {logs.length === 0 ? (
          <TableRow>
            <TableCell colSpan={5}>
              <Typography variant="body2" color="text.secondary" textAlign="center" py={2}>
                No hay cambios registrados aún.
              </Typography>
            </TableCell>
          </TableRow>
        ) : (
          logs.map((log) => (
            <TableRow key={log.id}>
              <TableCell>{log.parameterKey}</TableCell>
              <TableCell>{log.oldValue}</TableCell>
              <TableCell>{log.newValue}</TableCell>
              <TableCell>{log.changedById}</TableCell>
              <TableCell>{new Date(log.changedAt).toLocaleString()}</TableCell>
            </TableRow>
          ))
        )}
      </TableBody>
    </Table>
  )
}
