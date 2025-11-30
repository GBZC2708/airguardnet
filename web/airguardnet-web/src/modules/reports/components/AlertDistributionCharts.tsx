import { Card, CardContent, LinearProgress, Stack, Typography } from '@mui/material'

export const AlertDistributionCharts = ({ data }: { data: { label: string; value: number }[] }) => {
  const total = data.reduce((acc, item) => acc + item.value, 0)
  return (
    <Card>
      <CardContent>
        <Typography variant="h6" fontWeight={700} mb={2}>
          Alertas por severidad
        </Typography>
        <Stack spacing={1}>
          {data.map((item) => (
            <Stack key={item.label} direction="row" alignItems="center" spacing={2}>
              <Typography variant="body2" sx={{ width: 120 }}>
                {item.label}
              </Typography>
              <LinearProgress
                variant="determinate"
                value={total ? (item.value / total) * 100 : 0}
                color={item.label === 'Críticas' ? 'error' : item.label === 'Altas' ? 'warning' : 'primary'}
                sx={{ flex: 1, height: 10, borderRadius: 5 }}
              />
              <Typography variant="caption">{item.value}</Typography>
            </Stack>
          ))}
        </Stack>
      </CardContent>
    </Card>
  )
}
