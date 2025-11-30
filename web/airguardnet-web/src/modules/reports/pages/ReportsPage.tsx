import { useEffect, useState } from 'react'
import { LinearProgress, Stack, Typography, Box } from '@mui/material'
import { reportApi } from '../../../core/api/reportApi'
import { alertApi } from '../../../core/api/alertApi'
import type { UsageReport, Alert } from '../../../core/types'
import { KpiCards } from '../components/KpiCards'
import { TimeSeriesCharts } from '../components/TimeSeriesCharts'
import { AlertDistributionCharts } from '../components/AlertDistributionCharts'

export const ReportsPage = () => {
  const [reports, setReports] = useState<UsageReport[]>([])
  const [alerts, setAlerts] = useState<Alert[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const load = async () => {
      const [reportsRes, alertsRes] = await Promise.all([reportApi.getUsageReports(), alertApi.getAlerts()])
      setReports(reportsRes || [])
      setAlerts(alertsRes || [])
      setLoading(false)
    }
    load()
  }, [])

  if (loading) return <LinearProgress />

  const latest = reports[0]
  const kpis = [
    { label: 'Usuarios', value: latest?.totalUsers ?? '--' },
    { label: 'Dispositivos', value: latest?.totalDevices ?? '--' },
    { label: 'Lecturas', value: latest?.totalReadings ?? '--' },
    { label: 'Alertas', value: latest?.totalAlerts ?? alerts.length },
  ]

  const series = reports.slice(0, 7).map((r, idx) => {
    let avg = 0
    try {
      const parsed = JSON.parse(r.datosResumen || '{}')
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
      <KpiCards data={kpis} />
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '2fr 1fr' }, gap: 2 }}>
        <TimeSeriesCharts series={series} />
        <AlertDistributionCharts data={alertDistribution} />
      </Box>
    </Stack>
  )
}
