import { useState } from 'react'
import { Alert, Box, Button, Checkbox, FormControlLabel, Link, Stack, TextField, Typography } from '@mui/material'
import { useAuth } from '../hooks/useAuth'

export const LoginForm = () => {
  const { login, loading } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    try {
      await login({ email, password })
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Error al iniciar sesión'
      setError(message)
    }
  }

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <TextField
        label="Correo electrónico"
        fullWidth
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />
      <TextField
        label="Contraseña"
        type="password"
        fullWidth
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <Stack direction="row" alignItems="center" justifyContent="space-between">
        <FormControlLabel control={<Checkbox />} label="Recordarme" />
        <Link href="/forgot-password" underline="hover" color="primary.light">
          ¿Olvidaste tu contraseña?
        </Link>
      </Stack>
      {error && <Alert severity="error">{error}</Alert>}
      <Button type="submit" variant="contained" size="large" disabled={loading}>
        {loading ? 'Ingresando...' : 'Iniciar Sesión'}
      </Button>
      <Typography variant="body2" color="text.secondary">
        Al continuar aceptas nuestras políticas de monitoreo ambiental.
      </Typography>
    </Box>
  )
}
