import { useEffect, useMemo, useState } from 'react'
import { Alert, Box, Button, LinearProgress, Stack, TextField, Typography } from '@mui/material'
import { configApi } from '../../../core/api/configApi'
import type { ConfigParameter } from '../../../core/types'

export const ConfigForm = () => {
  const [params, setParams] = useState<ConfigParameter[]>([])
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [fieldError, setFieldError] = useState<string | null>(null)
  const [successMessage, setSuccessMessage] = useState<string | null>(null)

  const offlineParam = useMemo(() => params.find((param) => param.key === 'DEVICE_OFFLINE_MINUTES'), [params])
  const [offlineValue, setOfflineValue] = useState('')

  useEffect(() => {
    const load = async () => {
      setLoading(true)
      setError(null)
      try {
        const data = await configApi.getConfigParameters()
        setParams(data || [])
        const offline = data?.find((param) => param.key === 'DEVICE_OFFLINE_MINUTES')
        if (offline) {
          setOfflineValue(String(offline.value ?? ''))
        }
      } catch (err) {
        const message = err instanceof Error ? err.message : 'No se pudieron cargar los parámetros.'
        setError(message)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  const validate = (value: string) => {
    if (!value) return 'Ingresa un valor en minutos.'
    const numeric = Number(value)
    if (!Number.isInteger(numeric) || numeric < 1) return 'Usa un número entero mayor o igual a 1.'
    return ''
  }

  const handleSave = async () => {
    const validation = validate(offlineValue)
    setFieldError(validation || null)
    setSuccessMessage(null)
    if (validation) return

    if (!offlineParam) {
      setFieldError('La configuración no está disponible en esta versión.')
      return
    }

    setSaving(true)
    setError(null)
    try {
      await configApi.updateConfigParameter(offlineParam.key, offlineValue)
      setSuccessMessage('Parámetro actualizado exitosamente.')
    } catch (err) {
      const message = err instanceof Error ? err.message : 'No se pudo actualizar el parámetro.'
      setError(message)
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <LinearProgress />

  if (!offlineParam) {
    return <Alert severity="info">Configuración aún no disponible en esta versión.</Alert>
  }

  return (
    <Stack spacing={2}>
      <Typography variant="h6" fontWeight={700}>
        Parámetros del sistema
      </Typography>
      {error && <Alert severity="error">{error}</Alert>}
      {successMessage && <Alert severity="success">{successMessage}</Alert>}
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'repeat(2, 1fr)' }, gap: 2 }}>
        <TextField
          key={offlineParam.id}
          fullWidth
          label="Minutos para marcar dispositivo como OFFLINE"
          value={offlineValue}
          type="number"
          onChange={(e) => setOfflineValue(e.target.value)}
          error={!!fieldError}
          helperText={fieldError || 'Ejemplo: 15 minutos significa que si un dispositivo no envía lecturas en 15 minutos, se marcará como “Offline”.'}
          inputProps={{ min: 1 }}
        />
      </Box>
      <Stack direction="row" justifyContent="flex-end">
        <Button
          variant="contained"
          onClick={handleSave}
          disabled={saving}
          sx={{ minWidth: 160, transition: 'transform 160ms ease', '&:hover': { transform: 'translateY(-1px)' } }}
        >
          {saving ? 'Guardando...' : 'Guardar cambios'}
        </Button>
      </Stack>
    </Stack>
  )
}
