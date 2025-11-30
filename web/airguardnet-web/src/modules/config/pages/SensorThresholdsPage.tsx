import { Stack, Typography } from '@mui/material'
import { SensorConfigTable } from '../components/SensorConfigTable'

export const SensorThresholdsPage = () => (
  <Stack spacing={2}>
    <Typography variant="h5" fontWeight={800}>
      Umbrales de sensores
    </Typography>
    <SensorConfigTable />
  </Stack>
)
