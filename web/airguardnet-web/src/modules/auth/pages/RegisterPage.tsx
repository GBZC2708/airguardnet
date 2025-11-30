import { Alert, Box, Button, TextField, Typography } from '@mui/material'

export const RegisterPage = () => {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <Typography variant="h5" fontWeight={800}>
        Registro
      </Typography>
      <Alert severity="info">Solo administradores pueden registrar usuarios. TODO: integrar endpoint /register.</Alert>
      <TextField label="Nombre" fullWidth disabled />
      <TextField label="Apellido" fullWidth disabled />
      <TextField label="Correo" fullWidth disabled />
      <Button variant="contained" disabled>
        Registrar
      </Button>
    </Box>
  )
}
