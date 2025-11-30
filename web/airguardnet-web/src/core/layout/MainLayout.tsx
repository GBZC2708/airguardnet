import { useMemo, useState } from 'react'
import { Outlet, NavLink, useLocation } from 'react-router-dom'
import {
  AppBar,
  Avatar,
  Badge,
  Box,
  Button,
  Chip,
  Divider,
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Stack,
  Toolbar,
  Typography,
  useTheme,
  Paper
} from '@mui/material'
import DashboardIcon from '@mui/icons-material/Dashboard'
import DevicesIcon from '@mui/icons-material/PrecisionManufacturing'
import WarningIcon from '@mui/icons-material/WarningAmber'
import AssessmentIcon from '@mui/icons-material/Assessment'
import GroupIcon from '@mui/icons-material/Group'
import SettingsIcon from '@mui/icons-material/Settings'
import LibraryBooksIcon from '@mui/icons-material/LibraryBooks'
import ListAltIcon from '@mui/icons-material/ListAlt'
import ScienceIcon from '@mui/icons-material/Science'
import MenuIcon from '@mui/icons-material/Menu'
import NotificationsIcon from '@mui/icons-material/Notifications'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import ArrowRightIcon from '@mui/icons-material/ArrowRightAlt'
import { useAuth } from '../../modules/auth/hooks/useAuth'

const drawerWidth = 280

const navItems = [
  { label: 'Dashboard', to: '/dashboard', icon: <DashboardIcon /> },
  { label: 'Dispositivos', to: '/devices', icon: <DevicesIcon /> },
  { label: 'Alertas', to: '/alerts', icon: <WarningIcon /> },
  { label: 'Reportes', to: '/reports', icon: <AssessmentIcon /> },
  { label: 'Usuarios', to: '/users', icon: <GroupIcon /> },
  { label: 'Configuración', to: '/config/system', icon: <SettingsIcon /> },
  { label: 'Documentación', to: '/docs', icon: <LibraryBooksIcon /> },
  { label: 'Logs', to: '/logs/system', icon: <ListAltIcon /> },
  { label: 'Simulación', to: '/simulation', icon: <ScienceIcon /> },
]

const notificationsMock = [
  { id: 1, title: 'Nivel de polvo crítico', time: 'hace 5 min', severity: 'CRITICAL' },
  { id: 2, title: 'Alerta preventiva activada', time: 'hace 1 h', severity: 'HIGH' },
  { id: 3, title: 'Equipo sincronizado', time: 'ayer', severity: 'MEDIUM' }
]

export const MainLayout = () => {
  const { user, logout } = useAuth()
  const theme = useTheme()
  const [open, setOpen] = useState(false)
  const [notifOpen, setNotifOpen] = useState(false)
  const location = useLocation()

  const activeTitle = useMemo(() => {
    const match = navItems.find((item) => location.pathname.startsWith(item.to))
    return match?.label ?? 'AirGuardNet'
  }, [location.pathname])

  const drawer = (
    <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Box px={3} py={3}>
        <Typography variant="h5" fontWeight={700} color="primary.main">
          AirGuardNet
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Monitoreo de polvo en minería
        </Typography>
      </Box>
      <Box px={3} pb={2}>
        <Paper
          variant="outlined"
          sx={{
            borderColor: 'rgba(0,229,255,0.4)',
            background: 'rgba(0,229,255,0.06)',
            p: 2,
            borderRadius: 3,
          }}
        >
          <Stack direction="row" spacing={2} alignItems="center">
            <Avatar>{user?.name?.charAt(0) || 'A'}</Avatar>
            <Box>
              <Typography fontWeight={700}>{user?.name || 'Usuario'}</Typography>
              <Chip size="small" label={user?.role || 'OPERADOR'} color="primary" variant="outlined" sx={{ mt: 0.5 }} />
            </Box>
          </Stack>
        </Paper>
      </Box>
      <Divider sx={{ borderColor: 'rgba(255,255,255,0.06)' }} />
      <List sx={{ flexGrow: 1, px: 1 }}>
        {navItems.map((item) => (
          <ListItemButton
            key={item.to}
            component={NavLink}
            to={item.to}
            selected={location.pathname.startsWith(item.to)}
            sx={{
              my: 0.5,
              borderRadius: 2,
              '&.Mui-selected': {
                backgroundColor: 'rgba(0,229,255,0.12)',
                borderLeft: `4px solid ${theme.palette.primary.main}`,
                ml: -1,
              },
            }}
          >
            <ListItemIcon sx={{ color: 'text.secondary' }}>{item.icon}</ListItemIcon>
            <ListItemText primary={item.label} />
          </ListItemButton>
        ))}
      </List>
      <Divider sx={{ borderColor: 'rgba(255,255,255,0.06)' }} />
      <Box px={3} py={2}>
        <Stack direction="row" alignItems="center" spacing={1} mb={1}>
          <CheckCircleIcon color="success" fontSize="small" />
          <Typography variant="body2">Sincronizado</Typography>
        </Stack>
        <Button fullWidth variant="outlined" color="error" onClick={logout} sx={{ borderRadius: 2 }}>
          Cerrar sesión
        </Button>
      </Box>
    </Box>
  )

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      <AppBar
        position="fixed"
        sx={{
          background: '#0d1628',
          borderBottom: '1px solid rgba(255,255,255,0.06)',
          ml: { sm: `${drawerWidth}px` },
          width: { sm: `calc(100% - ${drawerWidth}px)` },
        }}
      >
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Stack direction="row" spacing={2} alignItems="center">
            <IconButton color="inherit" edge="start" onClick={() => setOpen(!open)} sx={{ display: { sm: 'none' } }}>
              <MenuIcon />
            </IconButton>
            <Typography variant="h6" fontWeight={700} color="white">
              {activeTitle}
            </Typography>
            <Chip label="Mina Norte" color="primary" variant="outlined" size="small" />
          </Stack>
          <Stack direction="row" spacing={2} alignItems="center">
            <IconButton color="inherit" onClick={() => setNotifOpen((p) => !p)}>
              <Badge color="error" variant="dot">
                <NotificationsIcon />
              </Badge>
            </IconButton>
            <Avatar>{user?.name?.charAt(0) || 'A'}</Avatar>
          </Stack>
        </Toolbar>
      </AppBar>

      <Box component="nav" sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}>
        <Drawer
          variant="temporary"
          open={open}
          onClose={() => setOpen(false)}
          ModalProps={{ keepMounted: true }}
          sx={{
            display: { xs: 'block', sm: 'none' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth, background: '#0b1324' },
          }}
        >
          {drawer}
        </Drawer>
        <Drawer
          variant="permanent"
          sx={{
            display: { xs: 'none', sm: 'block' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth, background: '#0b1324' },
          }}
          open
        >
          {drawer}
        </Drawer>
      </Box>

      <Box component="main" sx={{ flexGrow: 1, p: 3, mt: 8 }}>
        <Outlet />
      </Box>

      <Drawer
        anchor="right"
        open={notifOpen}
        onClose={() => setNotifOpen(false)}
        sx={{ '& .MuiDrawer-paper': { width: 360, background: '#0f1b2f' } }}
      >
        <Box p={3}>
          <Typography variant="h6" fontWeight={700} mb={2}>
            Notificaciones
          </Typography>
          <Stack spacing={2}>
            {notificationsMock.map((notif) => (
              <Paper key={notif.id} sx={{ p: 2, borderRadius: 2, bgcolor: 'background.paper' }}>
                <Stack direction="row" spacing={2} alignItems="center">
                  <Avatar sx={{ bgcolor: notif.severity === 'CRITICAL' ? 'error.main' : 'warning.main', width: 32, height: 32 }}>
                    <ArrowRightIcon />
                  </Avatar>
                  <Box>
                    <Typography fontWeight={600}>{notif.title}</Typography>
                    <Typography variant="caption" color="text.secondary">
                      {notif.time}
                    </Typography>
                  </Box>
                </Stack>
              </Paper>
            ))}
          </Stack>
        </Box>
      </Drawer>
    </Box>
  )
}

export default MainLayout
