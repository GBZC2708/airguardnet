import { Card, CardContent, LinearProgress, Stack, Typography } from '@mui/material'

interface Props {
  data: { pm25: number; riskIndex: number; airQualityPercent: number }[]
}

export const SimulationLiveChart = ({ data }: Props) => {
  const latest = data[data.length - 1]
  return (
    <Card>
      <CardContent>
        <Typography variant="h6" fontWeight={700} mb={2}>
          Simulación en tiempo real
        </Typography>
        <Typography variant="body2" color="text.secondary" mb={1}>
          Último PM2.5: {latest?.pm25.toFixed(1) ?? '--'} µg/m³
        </Typography>
        <Stack spacing={1}>
          {data.slice(-6).map((point, idx) => (
            <Stack key={idx} direction="row" spacing={2} alignItems="center">
              <Typography variant="caption" sx={{ width: 60 }}>
                T-{data.length - idx}
              </Typography>
              <LinearProgress
                variant="determinate"
                value={Math.min((point.pm25 / 200) * 100, 100)}
                color={point.pm25 > 150 ? 'error' : point.pm25 > 80 ? 'warning' : 'primary'}
                sx={{ flex: 1, height: 8, borderRadius: 4 }}
              />
              <Typography variant="caption">RI {point.riskIndex.toFixed(0)}%</Typography>
              <Typography variant="caption">AQ {point.airQualityPercent.toFixed(0)}%</Typography>
            </Stack>
          ))}
        </Stack>
      </CardContent>
    </Card>
  )
}
