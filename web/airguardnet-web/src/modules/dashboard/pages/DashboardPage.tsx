import { useEffect, useMemo, useState } from 'react'
import { Alert as MuiAlert, Box, Card, CardContent, Chip, LinearProgress, Stack, Typography } from '@mui/material'
import { reportApi } from '../../../core/api/reportApi'
import { alertApi } from '../../../core/api/alertApi'
import { deviceApi } from '../../../core/api/deviceApi'
import type { Alert, Device, UsageReport } from '../../../core/types'
import { SummaryCards } from '../components/SummaryCards'
import { ChartsSection } from '../components/ChartsSection'
import { GlobalRiskIndex } from '../components/GlobalRiskIndex'

export const DashboardPage = () => {
  const [report, setReport] = useState<UsageReport | null>(null)
  const [alerts, setAlerts] = useState<Alert[]>([])
  const [devices, setDevices] = useState<Device[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [reportsRes, alertsRes, devicesRes] = await Promise.all([
          reportApi.getUsageReports(),
          alertApi.getAlerts({ limit: 50 }),
          deviceApi.getDevices(),
        ])
        setReport(reportsRes?.[0] || null)
        setAlerts(alertsRes || [])
        setDevices(devicesRes || [])
      } catch (err) {
        const message = err instanceof Error ? err.message : 'No se pudo cargar el dashboard'
        setError(message)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  const criticalAlerts = useMemo(() => alerts.filter((a) => a.severity === 'CRITICAL'), [alerts])
  const pendingAlerts = useMemo(() => alerts.filter((a) => a.status !== 'RESOLVED'), [alerts])

  const pmTrend = alerts.slice(0, 8).map((a) => {
    const match = a.message.match(/([0-9]+\.?[0-9]*)/)
    return match ? Number(match[1]) : Math.random() * 120
  })

  const riskIndex = useMemo(() => {
    if (!devices.length) return 35
    const batteryScores = devices.map((d) => (d.lastBatteryLevel ? 100 - d.lastBatteryLevel : 20))
    const avg = batteryScores.reduce((a, b) => a + b, 0) / batteryScores.length
    return Math.min(Math.max(avg, 5), 95)
  }, [devices])

  const cards = [
    { title: 'Dispositivos', value: report?.totalDevices ?? devices.length ?? 0, helper: 'Monitoreo activo', color: 'primary' as const },
    { title: 'Lecturas', value: report?.totalReadings ?? '--', helper: 'Datos recientes', color: 'secondary' as const },
    { title: 'Alertas críticas', value: criticalAlerts.length, helper: 'Revisar de inmediato', color: 'error' as const },
    { title: 'Calidad de aire', value: `${100 - riskIndex}%`, helper: 'Promedio estimado', color: 'success' as const },
  ]

  const alertDistribution = [
    { label: 'Críticas', value: criticalAlerts.length },
    { label: 'Altas', value: alerts.filter((a) => a.severity === 'HIGH').length },
    { label: 'Medias', value: alerts.filter((a) => a.severity === 'MEDIUM').length },
  ]

  if (loading) return <LinearProgress />

  return (
    <Stack spacing={3}>
      <Card sx={{ borderRadius: 3, border: '1px solid rgba(0,229,255,0.2)' }}>
        <CardContent>
          <Stack direction={{ xs: 'column', md: 'row' }} justifyContent="space-between" spacing={2}>
            <Box>
              <Typography variant="h5" fontWeight={800}>
                Tu ubicación actual
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Túnel 7 - Zona B
              </Typography>
              <Chip label="Conexión en tiempo real" color="success" sx={{ mt: 1 }} />
            </Box>
            <Box>
              <Typography variant="body2" color="text.secondary">
                Alertas activas en tu zona
              </Typography>
              <Typography variant="h4" fontWeight={800}>
                {pendingAlerts.length}
              </Typography>
            </Box>
          </Stack>
        </CardContent>
      </Card>

      {error && <MuiAlert severity="error">{error}</MuiAlert>}

      <SummaryCards cards={cards} />

      <Box sx={{ display: 'grid', gap: 2, gridTemplateColumns: { xs: '1fr', md: '2fr 1fr' } }}>
        <ChartsSection pmTrend={pmTrend} alertDistribution={alertDistribution} />
        <GlobalRiskIndex value={riskIndex} />
      </Box>
    </Stack>
  )
}
