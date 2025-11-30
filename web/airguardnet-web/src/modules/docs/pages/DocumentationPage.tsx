import { useMemo, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Chip,
  Dialog,
  DialogContent,
  DialogTitle,
  Divider,
  Link,
  Stack,
  TextField,
  Typography,
  Button,
} from '@mui/material'

type DocCategory =
  | 'Primeros pasos'
  | 'Configuración de dispositivos'
  | 'Interpretación de datos de polvo'
  | 'Gestión de alertas'
  | 'API y Webhooks'

interface DocItem {
  id: string
  category: DocCategory
  title: string
  type: 'Guía' | 'Video tutorial'
  description: string
  details: string[]
  url?: string
}

const categories: DocCategory[] = [
  'Primeros pasos',
  'Configuración de dispositivos',
  'Interpretación de datos de polvo',
  'Gestión de alertas',
  'API y Webhooks',
]

const docs: DocItem[] = [
  {
    id: 'getting-started-overview',
    category: 'Primeros pasos',
    title: 'Tour completo del sistema',
    type: 'Guía',
    description: 'Recorrido por las principales pantallas y cómo navegar entre módulos.',
    details: [
      'Revisa el dashboard para identificar el estado general de tus dispositivos.',
      'Usa el menú lateral para saltar a Dispositivos, Alertas y Reportes.',
      'Consulta la sección de Configuración para ajustar parámetros críticos.',
    ],
    url: 'https://example.com/tour',
  },
  {
    id: 'first-login',
    category: 'Primeros pasos',
    title: 'Primer ingreso seguro',
    type: 'Guía',
    description: 'Configura tu contraseña y revisa los requerimientos de seguridad.',
    details: [
      'Utiliza una contraseña con al menos 10 caracteres y mezcla de símbolos.',
      'Habilita la opción de recordar sesión solo en dispositivos confiables.',
      'Verifica que tu rol (ADMIN/SUPERVISOR/OPERADOR) coincida con tus permisos.',
    ],
  },
  {
    id: 'device-setup',
    category: 'Configuración de dispositivos',
    title: 'Instalación de PMS5003',
    type: 'Video tutorial',
    description: 'Guía visual para conectar y calibrar el sensor PMS5003 en campo.',
    details: [
      'Prepara el equipo con filtros y cables asegurados.',
      'Conecta el sensor al gateway y valida la alimentación.',
      'Confirma la recepción de lecturas en la vista de Dispositivos.',
    ],
    url: 'https://example.com/pms5003',
  },
  {
    id: 'device-thresholds',
    category: 'Configuración de dispositivos',
    title: 'Umbrales recomendados por zona',
    type: 'Guía',
    description: 'Cómo definir valores recomendados y críticos para cada sensor.',
    details: [
      'Revisa el historial de alertas para definir puntos de corte realistas.',
      'Configura los umbrales en Configuración → Sensores y guarda los cambios.',
      'Monitorea las alertas generadas y ajusta si hay falsos positivos.',
    ],
  },
  {
    id: 'dust-insights',
    category: 'Interpretación de datos de polvo',
    title: 'Cómo leer los gráficos de partículas',
    type: 'Guía',
    description: 'Entiende las tendencias y los promedios móviles mostrados en el dashboard.',
    details: [
      'Identifica picos asociados a eventos operativos específicos.',
      'Usa los promedios móviles para suavizar lecturas y detectar patrones.',
      'Cruza la información con la ubicación de los equipos en campo.',
    ],
  },
  {
    id: 'alert-handling',
    category: 'Gestión de alertas',
    title: 'Escalamiento y cierre de alertas',
    type: 'Guía',
    description: 'Proceso recomendado para atender una alerta crítica y documentar acciones.',
    details: [
      'Confirma la vigencia del dispositivo y revisa su última comunicación.',
      'Asigna la alerta a un responsable y deja comentarios de mitigación.',
      'Cierra la alerta solo cuando la lectura vuelva a niveles aceptables.',
    ],
  },
  {
    id: 'webhooks-api',
    category: 'API y Webhooks',
    title: 'Integración vía API y webhooks',
    type: 'Guía',
    description: 'Pasos para consumir la API y configurar webhooks de notificación.',
    details: [
      'Genera un token desde la sección de usuarios con rol ADMIN.',
      'Consulta el API Gateway en http://localhost:8080/api para probar endpoints.',
      'Configura tus URLs de webhook y valida la entrega de eventos.',
    ],
    url: 'https://example.com/api-docs',
  },
]

export const DocumentationPage = () => {
  const [selectedCategory, setSelectedCategory] = useState<DocCategory | null>(null)
  const [search, setSearch] = useState('')
  const [selectedDoc, setSelectedDoc] = useState<DocItem | null>(null)

  const filtered = useMemo(
    () =>
      docs.filter((doc) => {
        const matchesCategory = !selectedCategory || doc.category === selectedCategory
        const term = search.toLowerCase()
        const matchesSearch = doc.title.toLowerCase().includes(term) || doc.description.toLowerCase().includes(term)
        return matchesCategory && matchesSearch
      }),
    [search, selectedCategory]
  )

  const handleSelectCategory = (category: DocCategory) => {
    setSelectedCategory((prev) => (prev === category ? null : category))
  }

  return (
    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '280px 1fr' }, gap: 2 }}>
      <Stack spacing={1}>
        <Typography variant="h6" fontWeight={700}>
          Categorías
        </Typography>
        {categories.map((cat) => (
          <Chip
            key={cat}
            label={cat}
            color={selectedCategory === cat ? 'primary' : 'default'}
            variant={selectedCategory === cat ? 'filled' : 'outlined'}
            onClick={() => handleSelectCategory(cat)}
            sx={{ justifyContent: 'flex-start' }}
          />
        ))}
      </Stack>
      <Stack spacing={2}>
        <TextField
          placeholder="Busca en la documentación..."
          fullWidth
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <Card sx={{ border: '1px solid rgba(0,229,255,0.16)' }}>
          <CardContent>
            <Typography variant="h5" fontWeight={800}>
              ¡Bienvenido a la Documentación!
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Filtra por categoría o busca palabras clave para encontrar la guía que necesitas. Cada tarjeta abre un resumen con
              pasos accionables.
            </Typography>
          </CardContent>
        </Card>
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'repeat(3, 1fr)' }, gap: 2 }}>
          {filtered.map((doc) => (
            <Card
              key={doc.id}
              onClick={() => setSelectedDoc(doc)}
              sx={{
                cursor: 'pointer',
                transition: 'transform 160ms ease, box-shadow 160ms ease',
                '&:hover': { transform: 'translateY(-2px)', boxShadow: 6 },
              }}
            >
              <CardContent>
                <Stack spacing={1}>
                  <Chip label={doc.type} size="small" color="secondary" variant="outlined" />
                  <Typography fontWeight={700}>{doc.title}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {doc.description}
                  </Typography>
                </Stack>
              </CardContent>
            </Card>
          ))}
        </Box>
        {filtered.length === 0 && (
          <Typography variant="body2" color="text.secondary">
            No se encontraron guías para el filtro o búsqueda ingresados.
          </Typography>
        )}
      </Stack>

      <Dialog open={!!selectedDoc} onClose={() => setSelectedDoc(null)} fullWidth maxWidth="sm">
        {selectedDoc && (
          <>
            <DialogTitle>{selectedDoc.title}</DialogTitle>
            <DialogContent dividers>
              <Stack spacing={2}>
                <Typography variant="subtitle2" color="text.secondary">
                  {selectedDoc.category} · {selectedDoc.type}
                </Typography>
                <Typography>{selectedDoc.description}</Typography>
                <Divider />
                <Stack spacing={1}>
                  {selectedDoc.details.map((step) => (
                    <Typography key={step} variant="body2">
                      • {step}
                    </Typography>
                  ))}
                </Stack>
                {selectedDoc.url && (
                  <Button
                    component={Link}
                    href={selectedDoc.url}
                    target="_blank"
                    rel="noopener"
                    variant="contained"
                    sx={{ alignSelf: 'flex-start' }}
                  >
                    Ver guía externa
                  </Button>
                )}
              </Stack>
            </DialogContent>
          </>
        )}
      </Dialog>
    </Box>
  )
}
