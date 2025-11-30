import { Box, Button, TextField, Typography } from '@mui/material'

export const ForgotPasswordPage = () => {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <Typography variant="h5" fontWeight={800}>Recuperar contraseña</Typography>
      <Typography variant="body2" color="text.secondary">
        Ingresa tu correo y te enviaremos instrucciones para restablecerla.
      </Typography>
      <TextField label="Correo electrónico" fullWidth />
      <Button variant="contained" disabled>
        Enviar instrucciones (pendiente backend)
      </Button>
    </Box>
  )
}
