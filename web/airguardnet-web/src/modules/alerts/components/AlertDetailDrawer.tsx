import { useEffect, useState } from 'react'
import type { Alert as AlertType, AlertStatus } from '../../../core/types'
import {
  Button,
  Divider,
  Drawer,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  Typography
} from '@mui/material'
import { alertApi } from '../../../core/api/alertApi'

interface Props {
  open: boolean
  alert?: AlertType | null
  onClose: () => void
  onUpdated: () => void
}

export const AlertDetailDrawer = ({ open, alert, onClose, onUpdated }: Props) => {
  const [status, setStatus] = useState<AlertStatus>('PENDING')

  useEffect(() => {
    if (alert) setStatus(alert.status)
  }, [alert])

  const handleUpdate = async () => {
    if (!alert) return
    await alertApi.updateAlertStatus(alert.id, status)
    onUpdated()
  }

  if (!alert) return null

  return (
    <Drawer anchor="right" open={open} onClose={onClose} sx={{ '& .MuiDrawer-paper': { width: 380, p: 3 } }}>
      <Stack spacing={2}>
        <Typography variant="h6" fontWeight={800}>
          Detalle de alerta
        </Typography>
        <Typography variant="body1" fontWeight={700}>
          {alert.message}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Creada: {new Date(alert.createdAt).toLocaleString()}
        </Typography>
        <Divider />
        <FormControl fullWidth>
          <InputLabel>Estado</InputLabel>
          <Select value={status} label="Estado" onChange={(e) => setStatus(e.target.value as AlertStatus)}>
            <MenuItem value="PENDING">Pendiente</MenuItem>
            <MenuItem value="IN_PROGRESS">En proceso</MenuItem>
            <MenuItem value="RESOLVED">Resuelta</MenuItem>
          </Select>
        </FormControl>
        <Button variant="contained" onClick={handleUpdate}>
          Actualizar estado
        </Button>
      </Stack>
    </Drawer>
  )
}
