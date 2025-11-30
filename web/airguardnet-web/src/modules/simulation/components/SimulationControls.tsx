import { Button, Stack } from '@mui/material'

interface Props {
  onModeChange: (mode: 'normal' | 'peak' | 'recover') => void
}

export const SimulationControls = ({ onModeChange }: Props) => (
  <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
    <Button variant="contained" onClick={() => onModeChange('normal')}>
      Normal
    </Button>
    <Button variant="contained" color="warning" onClick={() => onModeChange('peak')}>
      Pico de polvo
    </Button>
    <Button variant="outlined" onClick={() => onModeChange('recover')}>
      Volver a normalidad
    </Button>
  </Stack>
)
