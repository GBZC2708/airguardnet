import { useEffect, useState } from 'react'
import { IconButton, LinearProgress, Table, TableBody, TableCell, TableHead, TableRow, TextField } from '@mui/material'
import SaveIcon from '@mui/icons-material/Save'
import type { SensorConfig } from '../../../core/types'
import { configApi } from '../../../core/api/configApi'

export const SensorConfigTable = () => {
  const [configs, setConfigs] = useState<SensorConfig[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const load = async () => {
      const data = await configApi.getSensorConfigs()
      setConfigs(data || [])
      setLoading(false)
    }
    load()
  }, [])

  const handleSave = async (config: SensorConfig) => {
    await configApi.updateSensorConfig(config.id, config)
  }

  const updateField = (id: number, field: keyof SensorConfig, value: number | string) => {
    setConfigs((prev) => prev.map((cfg) => (cfg.id === id ? { ...cfg, [field]: value } : cfg)))
  }

  if (loading) return <LinearProgress />

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
        {configs.map((config) => (
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
        ))}
      </TableBody>
    </Table>
  )
}
