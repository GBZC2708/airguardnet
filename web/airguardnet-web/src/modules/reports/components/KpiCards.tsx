import { Box, Card, CardContent, Typography } from '@mui/material'

export const KpiCards = ({ data }: { data: { label: string; value: string | number }[] }) => (
  <Box sx={{ display: 'grid', gridTemplateColumns: { xs: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' }, gap: 2 }}>
    {data.map((item) => (
      <Card key={item.label}>
        <CardContent>
          <Typography variant="body2" color="text.secondary">
            {item.label}
          </Typography>
          <Typography variant="h5" fontWeight={800}>
            {item.value}
          </Typography>
        </CardContent>
      </Card>
    ))}
  </Box>
)
