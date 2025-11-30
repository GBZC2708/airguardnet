import { Chip, Stack } from '@mui/material'
import type { Alert } from '../../../core/types'

const filters: { label: string; value: Alert['severity'] | 'ALL' }[] = [
  { label: 'Todas', value: 'ALL' },
  { label: 'Críticas', value: 'CRITICAL' },
  { label: 'Altas', value: 'HIGH' },
  { label: 'Medias', value: 'MEDIUM' },
]

export const AlertFilters = ({ value, onChange }: { value: string; onChange: (v: string) => void }) => (
  <Stack direction="row" spacing={1} flexWrap="wrap">
    {filters.map((filter) => (
      <Chip
        key={filter.value}
        label={filter.label}
        color={value === filter.value ? 'primary' : 'default'}
        variant={value === filter.value ? 'filled' : 'outlined'}
        onClick={() => onChange(filter.value)}
      />
    ))}
  </Stack>
)
