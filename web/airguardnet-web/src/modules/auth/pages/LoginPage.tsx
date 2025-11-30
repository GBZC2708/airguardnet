import { Stack, Typography } from '@mui/material'
import { LoginForm } from '../components/LoginForm'

export const LoginPage = () => {
  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={800}>
        Inicia sesión para continuar
      </Typography>
      <Typography variant="body2" color="text.secondary">
        Gestiona el monitoreo de polvo, alertas y dispositivos de tu operación minera.
      </Typography>
      <LoginForm />
    </Stack>
  )
}
