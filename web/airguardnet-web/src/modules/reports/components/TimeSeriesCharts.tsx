import { Card, CardContent, LinearProgress, Stack, Typography } from '@mui/material'

export const TimeSeriesCharts = ({ series }: { series: { label: string; value: number }[] }) => (
  <Card>
    <CardContent>
      <Typography variant="h6" fontWeight={700} mb={2}>
        Promedio diario de PM2.5
      </Typography>
      {series.length === 0 ? (
        <Typography variant="body2" color="text.secondary">
          Sin datos suficientes para generar el gráfico.
        </Typography>
      ) : (
        <Stack spacing={1}>
          {series.map((item) => (
            <Stack key={item.label} direction="row" alignItems="center" spacing={2}>
              <Typography variant="body2" sx={{ width: 120 }}>
                {item.label}
              </Typography>
              <LinearProgress
                variant="determinate"
                value={Math.min((item.value / 200) * 100, 100)}
                color={item.value > 150 ? 'error' : item.value > 80 ? 'warning' : 'primary'}
                sx={{ flex: 1, height: 10, borderRadius: 5 }}
              />
              <Typography variant="caption">{item.value.toFixed(1)} µg/m³</Typography>
            </Stack>
          ))}
        </Stack>
      )}
    </CardContent>
  </Card>
)
