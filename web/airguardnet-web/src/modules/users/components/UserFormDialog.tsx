import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Alert } from '@mui/material'

export const UserFormDialog = ({ open, onClose }: { open: boolean; onClose: () => void }) => {
  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Nuevo usuario</DialogTitle>
      <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
        <Alert severity="info">TODO: integrar creación de usuarios con endpoint correspondiente.</Alert>
        <TextField label="Nombre" fullWidth disabled />
        <TextField label="Apellido" fullWidth disabled />
        <TextField label="Correo" fullWidth disabled />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cerrar</Button>
        <Button variant="contained" disabled>
          Guardar
        </Button>
      </DialogActions>
    </Dialog>
  )
}
