import { Stack, Typography } from '@mui/material'
import { ChangeLogTable } from '../components/ChangeLogTable'
import { ConfigNavigation } from '../components/ConfigNavigation'

export const ConfigChangeLogPage = () => (
  <Stack spacing={2}>
    <Typography variant="h5" fontWeight={800}>
      Historial de cambios de configuración
    </Typography>
    <Typography variant="body2" color="text.secondary">
      Consulta las modificaciones realizadas a los parámetros del sistema para auditar quién realizó el cambio y cuándo se
      aplicó.
    </Typography>
    <ConfigNavigation />
    <ChangeLogTable />
  </Stack>
)
