import { Stack, Typography } from '@mui/material'
import { ConfigForm } from '../components/ConfigForm'
import { ConfigNavigation } from '../components/ConfigNavigation'

export const SystemConfigPage = () => (
  <Stack spacing={2}>
    <Typography variant="h5" fontWeight={800}>
      Configuración del sistema
    </Typography>
    <Typography variant="body1" color="text.secondary">
      Parámetro que define cuántos minutos sin comunicación convierten un dispositivo en OFFLINE.
    </Typography>
    <Typography variant="body2" color="text.secondary">
      Ajusta este valor según la criticidad de tu operación. Un tiempo más corto detectará más rápido los dispositivos sin
      señal, pero podría marcar como offline a equipos con conectividad intermitente.
    </Typography>
    <ConfigNavigation />
    <ConfigForm />
  </Stack>
)
