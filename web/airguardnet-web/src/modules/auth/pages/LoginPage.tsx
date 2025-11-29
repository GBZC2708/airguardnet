import { Box, Button, TextField, Typography } from '@mui/material'

export const LoginPage = () => {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <Typography variant="h6">Ingreso a AirGuardNet</Typography>
      <TextField label="Email" fullWidth />
      <TextField label="Contraseña" type="password" fullWidth />
      <Button variant="contained">Ingresar</Button>
    </Box>
  )
}
