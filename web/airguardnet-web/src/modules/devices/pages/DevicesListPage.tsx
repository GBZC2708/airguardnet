import { useEffect, useMemo, useState } from 'react'
import { LinearProgress, Stack, Typography, Box } from '@mui/material'
import { deviceApi } from '../../../core/api/deviceApi'
import type { Device } from '../../../core/types'
import { DeviceCard } from '../components/DeviceCard'
import { DeviceFilters } from '../components/DeviceFilters'

export const DevicesListPage = () => {
  const [devices, setDevices] = useState<Device[]>([])
  const [filter, setFilter] = useState<string>('ALL')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchDevices = async () => {
      try {
        const data = await deviceApi.getDevices()
        setDevices(data)
      } finally {
        setLoading(false)
      }
    }
    fetchDevices()
  }, [])

  const filteredDevices = useMemo(() => {
    if (filter === 'ALL') return devices
    return devices.filter((d) => d.status === filter)
  }, [devices, filter])

  if (loading) return <LinearProgress />

  return (
    <Stack spacing={3}>
      <Stack direction={{ xs: 'column', sm: 'row' }} justifyContent="space-between" alignItems={{ xs: 'flex-start', sm: 'center' }} spacing={2}>
        <div>
          <Typography variant="h5" fontWeight={800}>
            Dispositivos
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Monitoreo en tiempo real de sensores PMS5003
          </Typography>
        </div>
        <DeviceFilters value={filter} onChange={setFilter} />
      </Stack>
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(3, 1fr)' }, gap: 2 }}>
        {filteredDevices.map((device) => (
          <DeviceCard key={device.id} device={device} />
        ))}
      </Box>
    </Stack>
  )
}
