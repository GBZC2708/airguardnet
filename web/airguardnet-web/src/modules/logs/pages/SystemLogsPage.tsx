import { useEffect, useState } from 'react'
import { Alert, LinearProgress, Stack, Typography } from '@mui/material'
import { logsApi } from '../../../core/api/logsApi'
import type { SystemLog } from '../../../core/types'
import { LogsTable } from '../components/LogsTable'

export const SystemLogsPage = () => {
  const [logs, setLogs] = useState<SystemLog[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const load = async () => {
      const data = await logsApi.getSystemLogs()
      setLogs(data)
      setLoading(false)
    }
    load()
  }, [])

  if (loading) return <LinearProgress />

  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={800}>
        Logs del sistema
      </Typography>
      <LogsTable variant="system" data={logs} />
      {!logs.length && <Alert severity="info">No hay registros del sistema.</Alert>}
    </Stack>
  )
}
