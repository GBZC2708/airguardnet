import { useEffect, useState } from 'react'
import { Alert, LinearProgress, Stack, Typography, Box } from '@mui/material'
import { reportApi } from '../../../core/api/reportApi'
import { alertApi } from '../../../core/api/alertApi'
import type { UsageReport, Alert as AlertType } from '../../../core/types'
import { KpiCards } from '../components/KpiCards'
import { TimeSeriesCharts } from '../components/TimeSeriesCharts'
import { AlertDistributionCharts } from '../components/AlertDistributionCharts'

export const ReportsPage = () => {
  const [reports, setReports] = useState<UsageReport[]>([])
  const [alerts, setAlerts] = useState<AlertType[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const load = async () => {
      try {
        const [reportsRes, alertsRes] = await Promise.all([reportApi.getUsageReports(), alertApi.getAlerts()])
        setReports(reportsRes || [])
        setAlerts(alertsRes || [])
      } catch (err) {
        const message = err instanceof Error ? err.message : 'No se pudieron cargar los reportes'
        setError(message)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  if (loading) return <LinearProgress />

  const latest = reports[reports.length - 1]
  const kpis = [
    { label: 'Usuarios', value: latest?.totalUsers ?? 0 },
    { label: 'Dispositivos', value: latest?.totalDevices ?? 0 },
    { label: 'Lecturas', value: latest?.totalReadings ?? 0 },
    { label: 'Alertas', value: latest?.totalAlerts ?? alerts.length },
  ]

  const series = reports.slice(-7).map((r, idx) => {
    let avg = 0
    try {
      const parsed = typeof r.datosResumen === 'string' ? JSON.parse(r.datosResumen || '{}') : r.datosResumen ?? {}
      avg = Number(parsed.avgPm25 ?? 0)
    } catch (err) {
      avg = 0
    }
    return { label: `Día ${idx + 1}`, value: avg }
  })
  const alertDistribution = [
    { label: 'Críticas', value: alerts.filter((a) => a.severity === 'CRITICAL').length },
    { label: 'Altas', value: alerts.filter((a) => a.severity === 'HIGH').length },
    { label: 'Medias', value: alerts.filter((a) => a.severity === 'MEDIUM').length },
  ]

  return (
    <Stack spacing={3}>
      <Typography variant="h5" fontWeight={800}>
        Reportes de uso
      </Typography>
      {error && <Alert severity="error">{error}</Alert>}
      <KpiCards data={kpis} />
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '2fr 1fr' }, gap: 2 }}>
        <TimeSeriesCharts series={series} />
        <AlertDistributionCharts data={alertDistribution} />
      </Box>
    </Stack>
  )
}
