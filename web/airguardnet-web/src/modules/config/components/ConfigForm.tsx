import { LinearProgress, Stack, TextField, Typography, Box } from '@mui/material'
import { configApi } from '../../../core/api/configApi'
import type { ConfigParameter } from '../../../core/types'
import { useEffect, useState } from 'react'

export const ConfigForm = () => {
  const [params, setParams] = useState<ConfigParameter[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const load = async () => {
      const data = await configApi.getConfigParameters()
      setParams(data || [])
      setLoading(false)
    }
    load()
  }, [])

  const handleSave = async (key: string, value: string) => {
    await configApi.updateConfigParameter(key, value)
  }

  if (loading) return <LinearProgress />

  return (
    <Stack spacing={2}>
      <Typography variant="h6" fontWeight={700}>
        Parámetros del sistema
      </Typography>
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'repeat(2, 1fr)' }, gap: 2 }}>
        {params.map((param) => (
          <TextField
            key={param.id}
            fullWidth
            label={param.key}
            defaultValue={param.value}
            onBlur={(e) => handleSave(param.key, e.target.value)}
          />
        ))}
      </Box>
    </Stack>
  )
}
