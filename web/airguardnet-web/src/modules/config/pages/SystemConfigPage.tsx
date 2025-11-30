import { Stack, Typography } from '@mui/material'
import { ConfigForm } from '../components/ConfigForm'

export const SystemConfigPage = () => (
  <Stack spacing={2}>
    <Typography variant="h5" fontWeight={800}>
      Configuración del sistema
    </Typography>
    <ConfigForm />
  </Stack>
)
