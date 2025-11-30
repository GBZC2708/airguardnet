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
    if (!email && touched.email) return 'Ingresa tu correo electrónico.'
    const emailRegex = /.+@.+\..+/
    if (email && !emailRegex.test(email)) return 'Ingresa un correo electrónico válido.'
    return ''
  }, [email, touched.email])

  const passwordError = useMemo(() => {
    if (!password && touched.password) return 'Ingresa tu contraseña.'
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
      const defaultMessage =
        'Correo o contraseña incorrectos. Si el problema persiste, verifica que tu contraseña cumpla la política de seguridad.'
      const message = err instanceof Error ? err.message : defaultMessage
      const normalized = message.toLowerCase()
      const resolvedMessage = normalized.includes('conectar') || normalized.includes('servidor') ? message : defaultMessage
      setError(resolvedMessage)
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
        helperText={emailError || 'Usa tu correo corporativo para recibir notificaciones.'}
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
        helperText={passwordError || 'Usa al menos 8 caracteres combinando números, mayúsculas y símbolos.'}
      />
      <Stack direction="row" alignItems="center" justifyContent="space-between">
        <FormControlLabel control={<Checkbox />} label="Recordarme" />
        <Link href="/forgot-password" underline="hover" color="primary.light">
          ¿Olvidaste tu contraseña?
        </Link>
      </Stack>
      {error && <Alert severity="error">{error}</Alert>}
      <Button
        type="submit"
        variant="contained"
        size="large"
        disabled={loading || isInvalid}
        sx={{
          transition: 'transform 160ms ease, box-shadow 160ms ease',
          '&:hover': { transform: 'translateY(-1px)', boxShadow: 6 },
          '&:active': { transform: 'translateY(0)' },
        }}
      >
        {loading ? 'Ingresando...' : 'Iniciar Sesión'}
      </Button>
      <Typography variant="body2" color="text.secondary">
        Al continuar aceptas nuestras políticas de monitoreo ambiental.
      </Typography>
    </Box>
  )
}
