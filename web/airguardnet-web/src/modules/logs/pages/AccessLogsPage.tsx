import { useEffect, useState } from 'react'
import { Alert, Button, LinearProgress, Stack, Typography } from '@mui/material'
import { logsApi } from '../../../core/api/logsApi'
import type { AccessLog } from '../../../core/types'
import { LogsTable } from '../components/LogsTable'

export const AccessLogsPage = () => {
  const [logs, setLogs] = useState<AccessLog[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await logsApi.getAccessLogs()
      setLogs(data)
    } catch (err) {
      const message = err instanceof Error ? err.message : 'No se pudieron cargar los logs de acceso.'
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
        Logs de acceso
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
      {!loading && !error && <LogsTable variant="access" data={logs} />}
      {!loading && !error && !logs.length && (
        <Alert severity="info">No hay accesos registrados.</Alert>
      )}
    </Stack>
  )
}
