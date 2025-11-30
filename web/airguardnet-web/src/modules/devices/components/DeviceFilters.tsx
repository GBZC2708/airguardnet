import { Chip, Stack } from '@mui/material'
import type { Device } from '../../../core/types'

const statusLabels: { label: string; value: Device['status'] | 'ALL' }[] = [
  { label: 'Todos', value: 'ALL' },
  { label: 'Activos', value: 'ACTIVE' },
  { label: 'Advertencia', value: 'WARNING' },
  { label: 'Críticos', value: 'CRITICAL' },
  { label: 'Offline', value: 'OFFLINE' },
]

export const DeviceFilters = ({ value, onChange }: { value: string; onChange: (status: string) => void }) => {
  return (
    <Stack direction="row" spacing={1} flexWrap="wrap">
      {statusLabels.map((status) => (
        <Chip
          key={status.value}
          label={status.label}
          color={value === status.value ? 'primary' : 'default'}
          variant={value === status.value ? 'filled' : 'outlined'}
          onClick={() => onChange(status.value)}
        />
      ))}
    </Stack>
  )
}
