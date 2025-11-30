import { useEffect, useMemo, useState } from 'react'
import { Alert as MuiAlert, Card, CardContent, LinearProgress, Stack, Typography, Box } from '@mui/material'
import { alertApi } from '../../../core/api/alertApi'
import type { Alert } from '../../../core/types'
import { AlertsTable } from '../components/AlertsTable'
import { AlertFilters } from '../components/AlertFilters'
import { AlertDetailDrawer } from '../components/AlertDetailDrawer'

export const AlertsCenterPage = () => {
  const [alerts, setAlerts] = useState<Alert[]>([])
  const [filter, setFilter] = useState<string>('ALL')
  const [loading, setLoading] = useState(true)
  const [selected, setSelected] = useState<Alert | null>(null)

  const loadAlerts = async () => {
    const data = await alertApi.getAlerts()
    setAlerts(data)
    setLoading(false)
  }

  useEffect(() => {
    loadAlerts()
  }, [])

  const filteredAlerts = useMemo(() => {
    if (filter === 'ALL') return alerts
    return alerts.filter((a) => a.severity === filter)
  }, [alerts, filter])

  const total = alerts.length
  const critical = alerts.filter((a) => a.severity === 'CRITICAL').length
  const pending = alerts.filter((a) => a.status !== 'RESOLVED').length
  const resolved = alerts.filter((a) => a.status === 'RESOLVED').length

  if (loading) return <LinearProgress />

  return (
    <Stack spacing={3}>
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' }, gap: 2 }}>
        {[{ label: 'Total', value: total }, { label: 'Críticas', value: critical }, { label: 'Pendientes', value: pending }, { label: 'Resueltas', value: resolved }].map((kpi) => (
          <Card key={kpi.label}>
            <CardContent>
              <Typography variant="body2" color="text.secondary">
                {kpi.label}
              </Typography>
              <Typography variant="h5" fontWeight={800}>
                {kpi.value}
              </Typography>
            </CardContent>
          </Card>
        ))}
      </Box>

      <AlertFilters value={filter} onChange={setFilter} />

      <AlertsTable alerts={filteredAlerts} onSelect={(a) => setSelected(a)} />

      <AlertDetailDrawer open={!!selected} alert={selected} onClose={() => setSelected(null)} onUpdated={loadAlerts} />

      {!alerts.length && <MuiAlert severity="info">No hay alertas registradas</MuiAlert>}
    </Stack>
  )
}
