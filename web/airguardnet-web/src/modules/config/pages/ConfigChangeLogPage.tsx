import { Stack, Typography } from '@mui/material'
import { ChangeLogTable } from '../components/ChangeLogTable'

export const ConfigChangeLogPage = () => (
  <Stack spacing={2}>
    <Typography variant="h5" fontWeight={800}>
      Historial de cambios de configuración
    </Typography>
    <ChangeLogTable />
  </Stack>
)
