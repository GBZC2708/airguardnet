import { Card, CardContent, LinearProgress, Stack, Typography, Box } from '@mui/material'

interface ChartsSectionProps {
  pmTrend?: number[]
  alertDistribution?: { label: string; value: number }[]
}

export const ChartsSection = ({ pmTrend = [], alertDistribution = [] }: ChartsSectionProps) => {
  const averagePm = pmTrend.length ? pmTrend.reduce((a, b) => a + b, 0) / pmTrend.length : 0
  const totalAlerts = alertDistribution.reduce((acc, item) => acc + item.value, 0)

  return (
    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '2fr 1fr' }, gap: 2 }}>
      <Card>
        <CardContent>
          <Typography variant="h6" fontWeight={700} mb={1}>
            Tendencia PM2.5 (últimas horas)
          </Typography>
          <Typography variant="body2" color="text.secondary" mb={2}>
            Promedio reciente: {averagePm.toFixed(1)} µg/m³
          </Typography>
          <Stack spacing={1}>
            {pmTrend.slice(-6).map((value, idx) => (
              <Stack key={idx} direction="row" spacing={2} alignItems="center">
                <Typography variant="caption" sx={{ width: 60 }}>
                  T-{pmTrend.length - idx}h
                </Typography>
                <LinearProgress
                  variant="determinate"
                  value={Math.min((value / 250) * 100, 100)}
                  sx={{ flex: 1, height: 10, borderRadius: 5 }}
                  color={value > 150 ? 'error' : value > 80 ? 'warning' : 'success'}
                />
                <Typography variant="caption">{value.toFixed(1)} µg/m³</Typography>
              </Stack>
            ))}
          </Stack>
        </CardContent>
      </Card>
      <Card>
        <CardContent>
          <Typography variant="h6" fontWeight={700} mb={2}>
            Distribución de alertas
          </Typography>
          <Stack spacing={1}>
            {alertDistribution.map((item) => (
              <Stack key={item.label} direction="row" spacing={2} alignItems="center">
                <Typography variant="body2" sx={{ width: 90 }}>
                  {item.label}
                </Typography>
                <LinearProgress
                  variant="determinate"
                  value={totalAlerts ? (item.value / totalAlerts) * 100 : 0}
                  color={item.label === 'Críticas' ? 'error' : item.label === 'Altas' ? 'warning' : 'primary'}
                  sx={{ flex: 1, height: 10, borderRadius: 5 }}
                />
                <Typography variant="caption">{item.value}</Typography>
              </Stack>
            ))}
          </Stack>
        </CardContent>
      </Card>
    </Box>
  )
}
