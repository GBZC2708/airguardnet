import { Card, CardContent, LinearProgress, Stack, Typography } from '@mui/material'

export const GlobalRiskIndex = ({ value }: { value: number }) => {
  const percent = Math.min(Math.max(value, 0), 100)
  const color = percent > 70 ? 'error' : percent > 40 ? 'warning' : 'success'

  return (
    <Card>
      <CardContent>
        <Stack spacing={1}>
          <Typography variant="h6" fontWeight={700}>
            Índice Global de Riesgo
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Calculado a partir de lecturas recientes de PM y nivel de batería.
          </Typography>
          <LinearProgress
            variant="determinate"
            value={percent}
            color={color}
            sx={{ height: 12, borderRadius: 6 }}
          />
          <Typography variant="h4" fontWeight={800}>
            {percent.toFixed(0)}%
          </Typography>
        </Stack>
      </CardContent>
    </Card>
  )
}
