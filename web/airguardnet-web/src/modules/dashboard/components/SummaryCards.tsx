import { Card, CardContent, Stack, Typography, Chip, Box } from '@mui/material'

interface SummaryCardProps {
  title: string
  value: string | number
  helper?: string
  color?: 'primary' | 'secondary' | 'success' | 'warning' | 'error'
}

export const SummaryCards = ({ cards }: { cards: SummaryCardProps[] }) => {
  return (
    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' }, gap: 2 }}>
      {cards.map((card) => (
        <Card key={card.title}>
          <CardContent>
            <Stack spacing={1}>
              <Typography variant="body2" color="text.secondary">
                {card.title}
              </Typography>
              <Typography variant="h5" fontWeight={800}>
                {card.value}
              </Typography>
              {card.helper && (
                <Chip
                  label={card.helper}
                  color={card.color || 'primary'}
                  variant="outlined"
                  size="small"
                  sx={{ alignSelf: 'flex-start' }}
                />
              )}
            </Stack>
          </CardContent>
        </Card>
      ))}
    </Box>
  )
}
