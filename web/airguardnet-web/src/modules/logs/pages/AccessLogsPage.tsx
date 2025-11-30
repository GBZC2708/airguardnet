import { useEffect, useState } from 'react'
import { Alert, LinearProgress, Stack, Typography } from '@mui/material'
import { logsApi } from '../../../core/api/logsApi'
import type { AccessLog } from '../../../core/types'
import { LogsTable } from '../components/LogsTable'

export const AccessLogsPage = () => {
  const [logs, setLogs] = useState<AccessLog[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const load = async () => {
      const data = await logsApi.getAccessLogs()
      setLogs(data)
      setLoading(false)
    }
    load()
  }, [])

  if (loading) return <LinearProgress />

  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={800}>
        Logs de acceso
      </Typography>
      <LogsTable variant="access" data={logs} />
      {!logs.length && <Alert severity="info">No hay accesos registrados.</Alert>}
    </Stack>
  )
}
