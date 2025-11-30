import { Card, CardContent, Chip, Stack, TextField, Typography, Box } from '@mui/material'

const categories = ['Primeros pasos', 'Configuración de Dispositivos', 'Interpretación de datos de polvo', 'Gestión de alertas', 'API y Webhooks']

export const DocumentationPage = () => {
  return (
    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '300px 1fr' }, gap: 2 }}>
      <Stack spacing={1}>
        <Typography variant="h6" fontWeight={700}>
          Categorías
        </Typography>
        {categories.map((cat) => (
          <Chip key={cat} label={cat} color="primary" variant="outlined" />
        ))}
      </Stack>
      <Stack spacing={2}>
        <TextField placeholder="Busca en la documentación..." fullWidth />
        <Card>
          <CardContent>
            <Typography variant="h5" fontWeight={800}>
              ¡Bienvenido a la Documentación!
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Encuentra guías y tutoriales para desplegar y operar AirGuardNet centrado en polvo.
            </Typography>
          </CardContent>
        </Card>
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'repeat(3, 1fr)' }, gap: 2 }}>
          {['Tour completo del sistema', 'Instalación de PMS5003', 'Gestión de alertas'].map((video) => (
            <Card key={video}>
              <CardContent>
                <Typography fontWeight={700}>{video}</Typography>
                <Typography variant="caption" color="text.secondary">
                  Video tutorial
                </Typography>
              </CardContent>
            </Card>
          ))}
        </Box>
      </Stack>
    </Box>
  )
}
