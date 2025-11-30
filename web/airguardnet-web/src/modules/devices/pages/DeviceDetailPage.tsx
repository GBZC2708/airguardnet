import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Alert as MuiAlert, Card, CardContent, Chip, LinearProgress, Stack, Typography, Table, TableBody, TableCell, TableHead, TableRow, Box } from '@mui/material'
import { deviceApi } from '../../../core/api/deviceApi'
import type { Alert, Device, Reading } from '../../../core/types'

export const DeviceDetailPage = () => {
  const { id } = useParams()
  const deviceId = Number(id)
  const [device, setDevice] = useState<Device | null>(null)
  const [readings, setReadings] = useState<Reading[]>([])
  const [alerts, setAlerts] = useState<Alert[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [deviceRes, readingsRes, alertsRes] = await Promise.all([
          deviceApi.getDeviceById(deviceId),
          deviceApi.getDeviceReadings(deviceId),
          deviceApi.getDeviceAlerts(deviceId),
        ])
        setDevice(deviceRes)
        setReadings(readingsRes || [])
        setAlerts(alertsRes || [])
      } finally {
        setLoading(false)
      }
    }
    if (deviceId) fetchData()
  }, [deviceId])

  if (loading) return <LinearProgress />
  if (!device) return <MuiAlert severity="error">No se encontró el dispositivo</MuiAlert>

  return (
    <Stack spacing={3}>
      <Card>
        <CardContent>
          <Stack direction={{ xs: 'column', md: 'row' }} justifyContent="space-between" spacing={2}>
            <div>
              <Typography variant="h5" fontWeight={800}>
                {device.name}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                UID: {device.deviceUid}
              </Typography>
              <Chip label={device.status} color={device.status === 'CRITICAL' ? 'error' : device.status === 'WARNING' ? 'warning' : 'success'} sx={{ mt: 1 }} />
            </div>
            <div>
              <Typography variant="body2" color="text.secondary">
                Nivel de batería
              </Typography>
              <Typography variant="h4" fontWeight={800}>
                {device.lastBatteryLevel ?? 0}%
              </Typography>
            </div>
          </Stack>
        </CardContent>
      </Card>

      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '2fr 1fr' }, gap: 2 }}>
        <Card>
          <CardContent>
            <Typography variant="h6" fontWeight={700} mb={2}>
              Lecturas recientes
            </Typography>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>Fecha</TableCell>
                  <TableCell>PM1</TableCell>
                  <TableCell>PM2.5</TableCell>
                  <TableCell>PM10</TableCell>
                  <TableCell>Batería</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {readings.slice(0, 8).map((reading) => (
                  <TableRow key={reading.id}>
                    <TableCell>{new Date(reading.recordedAt).toLocaleString()}</TableCell>
                    <TableCell>{reading.pm1 ?? '--'}</TableCell>
                    <TableCell>{reading.pm25 ?? '--'}</TableCell>
                    <TableCell>{reading.pm10 ?? '--'}</TableCell>
                    <TableCell>{reading.batteryLevel ?? '--'}%</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
        <Card>
          <CardContent>
            <Typography variant="h6" fontWeight={700} mb={2}>
              Alertas del dispositivo
            </Typography>
            <Stack spacing={1}>
              {alerts.map((alert) => (
                <Card key={alert.id} sx={{ bgcolor: alert.severity === 'CRITICAL' ? 'rgba(255,82,82,0.1)' : 'background.paper' }}>
                  <CardContent>
                    <Typography fontWeight={700}>{alert.message}</Typography>
                    <Typography variant="caption" color="text.secondary">
                      {new Date(alert.createdAt).toLocaleString()} - {alert.status}
                    </Typography>
                  </CardContent>
                </Card>
              ))}
            </Stack>
          </CardContent>
        </Card>
      </Box>
    </Stack>
  )
}
