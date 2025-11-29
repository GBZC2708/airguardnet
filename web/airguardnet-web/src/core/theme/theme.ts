import { createTheme } from '@mui/material/styles'

export const theme = createTheme({
  palette: {
    mode: 'dark',
    background: {
      default: '#0a0f1a',
      paper: '#111827',
    },
    primary: { main: '#00e5ff' },
    secondary: { main: '#0091ea' },
    success: { main: '#00c853' },
    warning: { main: '#ffd600' },
    error: { main: '#ff1744' },
  },
  typography: {
    fontFamily: 'Inter, sans-serif',
  },
})
