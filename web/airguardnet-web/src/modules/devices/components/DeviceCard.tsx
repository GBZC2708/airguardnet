import { Card, CardContent, Chip, LinearProgress, Stack, Typography } from '@mui/material'
import type { Device } from '../../../core/types'
import { Link as RouterLink } from 'react-router-dom'

const statusColor: Record<Device['status'], 'success' | 'warning' | 'error' | 'default'> = {
  ACTIVE: 'success',
  WARNING: 'warning',
  CRITICAL: 'error',
  OFFLINE: 'default',
}

export const DeviceCard = ({ device }: { device: Device }) => {
  return (
    <Card component={RouterLink} to={`/devices/${device.id}`} sx={{ textDecoration: 'none' }}>
      <CardContent>
        <Stack spacing={1}>
          <Typography variant="h6" fontWeight={700}>
            {device.name}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            UID: {device.deviceUid}
          </Typography>
          <Chip label={device.status} color={statusColor[device.status]} variant="outlined" sx={{ alignSelf: 'flex-start' }} />
          <Stack spacing={0.5}>
            <Typography variant="caption" color="text.secondary">
              Última comunicación
            </Typography>
            <Typography variant="body2">{device.lastCommunicationAt || 'N/D'}</Typography>
          </Stack>
          <Stack spacing={0.5}>
            <Typography variant="caption" color="text.secondary">
              Nivel de batería
            </Typography>
            <LinearProgress
              variant="determinate"
              value={device.lastBatteryLevel ?? 0}
              color={(device.lastBatteryLevel ?? 0) > 60 ? 'success' : (device.lastBatteryLevel ?? 0) > 30 ? 'warning' : 'error'}
              sx={{ height: 10, borderRadius: 5 }}
            />
            <Typography variant="body2">{device.lastBatteryLevel ?? 0}%</Typography>
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  )
}
