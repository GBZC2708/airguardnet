import { Outlet, NavLink } from 'react-router-dom'
import {
  Box,
  Drawer,
  List,
  ListItemButton,
  ListItemText,
  AppBar,
  Toolbar,
  Typography
} from '@mui/material'

const drawerWidth = 220

const menuItems = [
  { label: 'Dashboard', to: '/dashboard' },
  { label: 'Dispositivos', to: '/devices' },
  { label: 'Alertas', to: '/alerts' },
  { label: 'Reportes', to: '/reports' },
  { label: 'Usuarios', to: '/users' },
  { label: 'Configuración', to: '/config' },
  { label: 'Documentación', to: '/docs' },
  { label: 'Logs', to: '/logs' },
  { label: 'Simulación', to: '/simulation' }
]

export const MainLayout = () => {
  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      <AppBar
        position="fixed"
        sx={{ zIndex: (theme) => theme.zIndex.drawer + 1, background: '#020617' }}
      >
        <Toolbar>
          <Typography variant="h6" noWrap component="div">
            AirGuardNet
          </Typography>
        </Toolbar>
      </AppBar>

      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            boxSizing: 'border-box',
            background: '#020617'
          }
        }}
      >
        <Toolbar />
        <List>
          {menuItems.map((item) => (
            <ListItemButton
              key={item.to}
              component={NavLink}
              to={item.to}
              sx={{ '&.active': { bgcolor: 'rgba(148,163,184,0.12)' } }}
            >
              <ListItemText primary={item.label} />
            </ListItemButton>
          ))}
        </List>
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  )
}
