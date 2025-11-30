import { useMemo, useState } from 'react'
import { Alert, Box, Button, Checkbox, FormControlLabel, Link, Stack, TextField, Typography } from '@mui/material'
import { useAuth } from '../hooks/useAuth'

export const LoginForm = () => {
  const { login, loading } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [touched, setTouched] = useState<{ email: boolean; password: boolean }>({ email: false, password: false })

  const emailError = useMemo(() => {
    if (!email && touched.email) return 'El correo es obligatorio'
    const emailRegex = /.+@.+\..+/
    if (email && !emailRegex.test(email)) return 'Ingresa un correo válido'
    return ''
  }, [email, touched.email])

  const passwordError = useMemo(() => {
    if (!password && touched.password) return 'La contraseña es obligatoria'
    return ''
  }, [password, touched.password])

  const isInvalid = !!emailError || !!passwordError || !email || !password

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setTouched({ email: true, password: true })
    if (isInvalid) return
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
        onBlur={() => setTouched((prev) => ({ ...prev, email: true }))}
        onChange={(e) => setEmail(e.target.value)}
        required
        error={!!emailError}
        helperText={emailError || ' '}
      />
      <TextField
        label="Contraseña"
        type="password"
        fullWidth
        value={password}
        onBlur={() => setTouched((prev) => ({ ...prev, password: true }))}
        onChange={(e) => setPassword(e.target.value)}
        required
        error={!!passwordError}
        helperText={passwordError || ' '}
      />
      <Stack direction="row" alignItems="center" justifyContent="space-between">
        <FormControlLabel control={<Checkbox />} label="Recordarme" />
        <Link href="/forgot-password" underline="hover" color="primary.light">
          ¿Olvidaste tu contraseña?
        </Link>
      </Stack>
      {error && <Alert severity="error">{error}</Alert>}
      <Button type="submit" variant="contained" size="large" disabled={loading || isInvalid}>
        {loading ? 'Ingresando...' : 'Iniciar Sesión'}
      </Button>
      <Typography variant="body2" color="text.secondary">
        Al continuar aceptas nuestras políticas de monitoreo ambiental.
      </Typography>
    </Box>
  )
}
