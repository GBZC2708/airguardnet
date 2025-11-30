import { Outlet } from 'react-router-dom'
import { Box, Paper, Stack, Typography, Chip, Divider } from '@mui/material'
import SensorsIcon from '@mui/icons-material/Sensors'
import ShieldIcon from '@mui/icons-material/Shield'
import TimelineIcon from '@mui/icons-material/Timeline'
import PrecisionManufacturingIcon from '@mui/icons-material/PrecisionManufacturing'

export const AuthLayout = () => {
  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default', display: 'flex', alignItems: 'center' }}>
      <Box sx={{ height: '100vh', display: 'grid', gridTemplateColumns: { xs: '1fr', md: '1fr 1fr' } }}>
        <Box display="flex" alignItems="center" justifyContent="center">
          <Box sx={{ width: '100%', maxWidth: 520, p: 6 }}>
            <Stack spacing={3}>
              <Box>
                <Typography variant="h4" fontWeight={800} color="primary.main">
                  AirGuardNet
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Sistema de Monitoreo Ambiental para Minería
                </Typography>
              </Box>
              <Typography variant="h5" fontWeight={700}>
                Acceso al Sistema
              </Typography>
              <Stack direction="row" spacing={2}>
                {['Administrador', 'Supervisor', 'Operador'].map((role) => (
                  <Paper
                    key={role}
                    sx={{
                      p: 2,
                      borderRadius: 3,
                      border: '1px solid rgba(0,229,255,0.4)',
                      bgcolor: 'rgba(0,229,255,0.04)',
                    }}
                  >
                    <Typography fontWeight={700}>{role}</Typography>
                    <Typography variant="caption" color="text.secondary">
                      Acceso estilizado
                    </Typography>
                  </Paper>
                ))}
              </Stack>
              <Outlet />
            </Stack>
          </Box>
        </Box>
        <Box sx={{ bgcolor: 'linear-gradient(135deg, #0f1a2e 0%, #09203f 100%)', color: 'white' }}>
          <Box sx={{ p: 6, height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center', gap: 4 }}>
            <Stack spacing={1}>
              <Paper sx={{ width: 96, height: 96, borderRadius: 4, bgcolor: 'rgba(0,229,255,0.12)', display: 'grid', placeItems: 'center' }}>
                <SensorsIcon sx={{ fontSize: 48, color: 'primary.main' }} />
              </Paper>
              <Typography variant="h4" fontWeight={800}>
                Tecnología de Monitoreo de Polvo Avanzado
              </Typography>
              <Typography variant="body1" color="rgba(255,255,255,0.7)">
                Protege a tu equipo con información en tiempo real de niveles de polvo en túneles y frentes de mina.
              </Typography>
            </Stack>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              {[{ label: 'Monitoreo 24/7', icon: <ShieldIcon /> }, { label: 'Precisión 99.9%', icon: <TimelineIcon /> }, { label: '+150 operaciones mineras', icon: <PrecisionManufacturingIcon /> }].map((kpi) => (
                <Paper key={kpi.label} sx={{ p: 2, borderRadius: 3, bgcolor: 'rgba(0,229,255,0.08)' }}>
                  <Stack direction="row" spacing={1} alignItems="center">
                    <Chip icon={kpi.icon} label={kpi.label} color="primary" variant="outlined" sx={{ borderRadius: 2 }} />
                  </Stack>
                </Paper>
              ))}
            </Stack>
            <Divider sx={{ borderColor: 'rgba(255,255,255,0.08)' }} />
            <Typography variant="body2" color="rgba(255,255,255,0.6)">
              Inspirado en las visuales de referencia, adaptado al monitoreo exclusivo de polvo con PMS5003.
            </Typography>
          </Box>
        </Box>
      </Box>
    </Box>
  )
}

export default AuthLayout
