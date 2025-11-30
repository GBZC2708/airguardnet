import { useEffect, useState } from 'react'
import { LinearProgress, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material'
import { configApi } from '../../../core/api/configApi'
import type { ConfigChangeLog } from '../../../core/types'

export const ChangeLogTable = () => {
  const [logs, setLogs] = useState<ConfigChangeLog[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const load = async () => {
      const data = await configApi.getConfigChangeLogs()
      setLogs(data || [])
      setLoading(false)
    }
    load()
  }, [])

  if (loading) return <LinearProgress />

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
        {logs.map((log) => (
          <TableRow key={log.id}>
            <TableCell>{log.parameterKey}</TableCell>
            <TableCell>{log.oldValue}</TableCell>
            <TableCell>{log.newValue}</TableCell>
            <TableCell>{log.changedById}</TableCell>
            <TableCell>{new Date(log.changedAt).toLocaleString()}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )
}
