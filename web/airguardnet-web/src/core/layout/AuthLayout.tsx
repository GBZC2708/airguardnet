import { Outlet } from 'react-router-dom'
import { Box, Container, Paper, Typography } from '@mui/material'

export const AuthLayout = () => {
  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        bgcolor: 'background.default'
      }}
    >
      <Container maxWidth="sm">
        <Paper sx={{ p: 4, borderRadius: 3 }}>
          <Typography variant="h5" mb={2}>
            AirGuardNet
          </Typography>
          <Outlet />
        </Paper>
      </Container>
    </Box>
  )
}
