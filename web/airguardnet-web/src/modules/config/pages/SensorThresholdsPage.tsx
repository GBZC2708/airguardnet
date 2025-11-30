import { Stack, Typography } from '@mui/material'
import { SensorConfigTable } from '../components/SensorConfigTable'
import { ConfigNavigation } from '../components/ConfigNavigation'

export const SensorThresholdsPage = () => (
  <Stack spacing={2}>
    <Typography variant="h5" fontWeight={800}>
      Umbrales de sensores
    </Typography>
    <Typography variant="body2" color="text.secondary">
      Define los valores recomendados y críticos para cada tipo de sensor de partículas. Estos umbrales impactan en la
      generación de alertas automáticas y en la visualización del estado de cada dispositivo.
    </Typography>
    <ConfigNavigation />
    <SensorConfigTable />
  </Stack>
)
