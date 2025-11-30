import { createTheme } from '@mui/material/styles'

export const theme = createTheme({
  palette: {
    mode: 'dark',
    background: {
      default: '#070f1d',
      paper: '#0d1628',
    },
    primary: { main: '#00e5ff' },
    secondary: { main: '#3f88c5' },
    success: { main: '#00c853' },
    warning: { main: '#ffd54f' },
    error: { main: '#ff5252' },
    text: {
      primary: '#e6f1ff',
      secondary: '#94a3b8',
    },
  },
  typography: {
    fontFamily: '"Inter", "Poppins", sans-serif',
    h4: { fontWeight: 800 },
    h5: { fontWeight: 700 },
    h6: { fontWeight: 700 },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 14,
          textTransform: 'none',
          fontWeight: 700,
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 18,
          background: '#0f1b2f',
          border: '1px solid rgba(255,255,255,0.04)',
        },
      },
    },
    MuiPaper: {
      defaultProps: {
        elevation: 0,
      },
      styleOverrides: {
        root: {
          backgroundImage: 'none',
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          fontWeight: 700,
        },
      },
    },
  },
})
