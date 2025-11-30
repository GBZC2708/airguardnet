import { Tabs, Tab } from '@mui/material'
import { useLocation, useNavigate } from 'react-router-dom'

const sections = [
  { label: 'Parámetros del sistema', value: '/config/system' },
  { label: 'Sensores y umbrales', value: '/config/sensors' },
  { label: 'Historial de cambios', value: '/config/logs' },
]

export const ConfigNavigation = () => {
  const location = useLocation()
  const navigate = useNavigate()

  const current = sections.find((section) => location.pathname.startsWith(section.value))?.value ?? sections[0].value

  return (
    <Tabs
      value={current}
      onChange={(_, value) => navigate(value)}
      textColor="secondary"
      indicatorColor="secondary"
      variant="scrollable"
      allowScrollButtonsMobile
    >
      {sections.map((section) => (
        <Tab key={section.value} label={section.label} value={section.value} />
      ))}
    </Tabs>
  )
}

