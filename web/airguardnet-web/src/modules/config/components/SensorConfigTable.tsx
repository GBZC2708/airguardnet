import { useEffect, useState } from 'react'
import { Alert, IconButton, LinearProgress, Table, TableBody, TableCell, TableHead, TableRow, TextField } from '@mui/material'
import SaveIcon from '@mui/icons-material/Save'
import type { SensorConfig } from '../../../core/types'
import { configApi } from '../../../core/api/configApi'

export const SensorConfigTable = () => {
  const [configs, setConfigs] = useState<SensorConfig[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const load = async () => {
      setError(null)
      try {
        const data = await configApi.getSensorConfigs()
        setConfigs(data || [])
      } catch (err) {
        const message = err instanceof Error ? err.message : 'No se pudieron cargar las configuraciones de sensores.'
        setError(message)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  const handleSave = async (config: SensorConfig) => {
    try {
      await configApi.updateSensorConfig(config.id, config)
    } catch (err) {
      const message = err instanceof Error ? err.message : 'No se pudo actualizar la configuración del sensor.'
      setError(message)
    }
  }

  const updateField = (id: number, field: keyof SensorConfig, value: number | string) => {
    setConfigs((prev) => prev.map((cfg) => (cfg.id === id ? { ...cfg, [field]: value } : cfg)))
  }

  if (loading) return <LinearProgress />

  if (error) return <Alert severity="error">{error}</Alert>

  return (
    <Table size="small">
      <TableHead>
        <TableRow>
          <TableCell>Sensor</TableCell>
          <TableCell>Recomendado</TableCell>
          <TableCell>Crítico</TableCell>
          <TableCell>Unidad</TableCell>
          <TableCell></TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {configs.length === 0 ? (
          <TableRow>
            <TableCell colSpan={5}>
              <Alert severity="info" sx={{ my: 1 }}>
                No hay umbrales configurados aún.
              </Alert>
            </TableCell>
          </TableRow>
        ) : (
          configs.map((config) => (
            <TableRow key={config.id}>
              <TableCell>{config.sensorType}</TableCell>
              <TableCell>
                <TextField
                  type="number"
                  value={config.recommendedMax}
                  onChange={(e) => updateField(config.id, 'recommendedMax', Number(e.target.value))}
                />
              </TableCell>
              <TableCell>
                <TextField
                  type="number"
                  value={config.criticalThreshold}
                  onChange={(e) => updateField(config.id, 'criticalThreshold', Number(e.target.value))}
                />
              </TableCell>
              <TableCell>{config.unit}</TableCell>
              <TableCell>
                <IconButton color="primary" onClick={() => handleSave(config)}>
                  <SaveIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))
        )}
      </TableBody>
    </Table>
  )
}
