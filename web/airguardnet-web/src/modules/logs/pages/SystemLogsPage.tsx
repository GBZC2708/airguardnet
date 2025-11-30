import { useEffect, useState } from 'react'
import { Alert, Button, LinearProgress, Stack, Typography } from '@mui/material'
import { logsApi } from '../../../core/api/logsApi'
import type { SystemLog } from '../../../core/types'
import { LogsTable } from '../components/LogsTable'

export const SystemLogsPage = () => {
  const [logs, setLogs] = useState<SystemLog[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await logsApi.getSystemLogs()
      setLogs(data)
    } catch (err) {
      const message = err instanceof Error ? err.message : 'No se pudieron cargar los logs del sistema.'
      setError(message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={800}>
        Logs del sistema
      </Typography>
      {loading && <LinearProgress />}
      {error && (
        <Alert
          severity="error"
          action=
            {(
              <Button color="inherit" size="small" onClick={load}>
                Reintentar
              </Button>
            )}
        >
          {error}
        </Alert>
      )}
      {!loading && !error && <LogsTable variant="system" data={logs} />}
      {!loading && !error && !logs.length && (
        <Alert severity="info">No hay registros del sistema.</Alert>
      )}
    </Stack>
  )
}
