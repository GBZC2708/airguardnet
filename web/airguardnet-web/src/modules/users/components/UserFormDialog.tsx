import { useEffect, useMemo, useState } from 'react'
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Alert, MenuItem, Stack, CircularProgress } from '@mui/material'
import type { Plan, User, UserRole } from '../../../core/types'
import { authApi, type RegisterPayload } from '../../../core/api/authApi'

interface Props {
  open: boolean
  onClose: () => void
  plans: Plan[]
  onUserCreated: (user: User) => void
}

const roles: UserRole[] = ['ADMIN', 'SUPERVISOR', 'TECNICO', 'OPERADOR']

export const UserFormDialog = ({ open, onClose, plans, onUserCreated }: Props) => {
  const [form, setForm] = useState<RegisterPayload>({
    name: '',
    lastName: '',
    email: '',
    role: 'OPERADOR',
    planId: plans[0]?.id ?? 1,
    password: ''
  })
  const [errors, setErrors] = useState<Partial<Record<keyof RegisterPayload, string>>>({})
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState<string | null>(null)

  useEffect(() => {
    if (plans.length) {
      setForm((prev) => ({
        ...prev,
        planId: plans.some((p) => p.id === prev.planId) ? prev.planId : plans[0].id
      }))
    }
  }, [plans])

  const validate = useMemo(() => (payload: RegisterPayload) => {
    const fieldErrors: Partial<Record<keyof RegisterPayload, string>> = {}
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/

    if (!payload.name.trim()) fieldErrors.name = 'El nombre es obligatorio.'
    if (!payload.lastName.trim()) fieldErrors.lastName = 'El apellido es obligatorio.'
    if (!payload.email.trim()) fieldErrors.email = 'El correo es obligatorio.'
    else if (!emailRegex.test(payload.email)) fieldErrors.email = 'Ingresa un correo electrónico válido.'
    if (!payload.role) fieldErrors.role = 'Selecciona un rol.'
    if (!payload.planId) fieldErrors.planId = 'Selecciona un plan.'
    if (!payload.password.trim()) fieldErrors.password = 'La contraseña es obligatoria.'
    else if (!passwordRegex.test(payload.password)) {
      fieldErrors.password = 'La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.'
    }
    return fieldErrors
  }, [])

  const handleChange = <K extends keyof RegisterPayload>(field: K, value: RegisterPayload[K]) => {
    setForm((prev) => ({ ...prev, [field]: value }))
    setErrors((prev) => ({ ...prev, [field]: undefined }))
  }

  const handleSubmit = async () => {
    const validationErrors = validate(form)
    setErrors(validationErrors)
    if (Object.keys(validationErrors).length > 0) return
    setSubmitting(true)
    setSubmitError(null)
    try {
      const created = await authApi.register(form)
      onUserCreated(created)
      setForm({ name: '', lastName: '', email: '', role: 'OPERADOR', planId: plans[0]?.id ?? 1, password: '' })
      onClose()
    } catch (err) {
      const message =
        err instanceof Error && err.message
          ? err.message
          : 'Ocurrió un error al crear el usuario. Verifica los datos e intenta nuevamente.'
      setSubmitError(message)
    } finally {
      setSubmitting(false)
    }
  }

  const isInvalid = useMemo(() => Object.keys(validate(form)).length > 0, [form, validate])

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Nuevo usuario</DialogTitle>
      <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
        {submitError && <Alert severity="error">{submitError}</Alert>}
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
          <TextField
            label="Nombre"
            fullWidth
            value={form.name}
            onChange={(e) => handleChange('name', e.target.value)}
            error={!!errors.name}
            helperText={errors.name || ' '}
            required
          />
          <TextField
            label="Apellido"
            fullWidth
            value={form.lastName}
            onChange={(e) => handleChange('lastName', e.target.value)}
            error={!!errors.lastName}
            helperText={errors.lastName || ' '}
            required
          />
        </Stack>
          <TextField
            label="Correo"
            fullWidth
            value={form.email}
            onChange={(e) => handleChange('email', e.target.value)}
            error={!!errors.email}
            helperText={errors.email || ' '}
            required
          />
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
          <TextField
            label="Rol"
            select
            fullWidth
            value={form.role}
            onChange={(e) => handleChange('role', e.target.value as UserRole)}
            error={!!errors.role}
            helperText={errors.role || ' '}
            required
          >
            {roles.map((role) => (
              <MenuItem key={role} value={role}>
                {role}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            label="Plan"
            select
            fullWidth
            value={form.planId}
            onChange={(e) => handleChange('planId', Number(e.target.value))}
            error={!!errors.planId}
            helperText={errors.planId || ' '}
            required
          >
            {plans.map((plan) => (
              <MenuItem key={plan.id} value={plan.id}>
                {plan.name}
              </MenuItem>
            ))}
          </TextField>
        </Stack>
        <TextField
          label="Contraseña"
          type="password"
          fullWidth
          value={form.password}
          onChange={(e) => handleChange('password', e.target.value)}
          error={!!errors.password}
          helperText={errors.password || 'Min. 8 caracteres, incluye mayúsculas, minúsculas y números.'}
          required
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cerrar</Button>
        <Button
          variant="contained"
          onClick={handleSubmit}
          disabled={submitting || isInvalid}
          startIcon={submitting ? <CircularProgress size={18} color="inherit" /> : undefined}
        >
          {submitting ? 'Guardando...' : 'Guardar'}
        </Button>
      </DialogActions>
    </Dialog>
  )
}
