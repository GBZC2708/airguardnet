import { useEffect, useState } from 'react'
import { Stack, Typography } from '@mui/material'
import { LoginForm } from '../components/LoginForm'

export const LoginPage = () => {
  const [visible, setVisible] = useState(false)

  useEffect(() => {
    setVisible(true)
  }, [])

  return (
    <Stack
      spacing={2}
      sx={{
        maxWidth: 520,
        width: '100%',
        opacity: visible ? 1 : 0,
        transform: visible ? 'translateY(0)' : 'translateY(-12px)',
        transition: 'opacity 260ms ease, transform 260ms ease',
      }}
    >
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
