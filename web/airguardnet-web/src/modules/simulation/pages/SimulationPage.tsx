import { useEffect, useRef, useState } from 'react'
import { Stack, Typography } from '@mui/material'
import { SimulationControls } from '../components/SimulationControls'
import { SimulationLiveChart } from '../components/SimulationLiveChart'

interface Point {
  pm25: number
  riskIndex: number
  airQualityPercent: number
}

const calculateRiskIndex = (pm25: number) => {
  if (pm25 > 150) return 90
  if (pm25 > 80) return 65
  if (pm25 > 50) return 40
  return 20
}

export const SimulationPage = () => {
  const [data, setData] = useState<Point[]>([{ pm25: 35, riskIndex: 20, airQualityPercent: 80 }])
  const modeRef = useRef<'normal' | 'peak' | 'recover'>('normal')

  useEffect(() => {
    const interval = setInterval(() => {
      setData((prev) => {
        const base = modeRef.current === 'peak' ? 140 : modeRef.current === 'recover' ? 60 : 40
        const pm25 = Math.max(10, base + Math.random() * 30 - 10)
        const riskIndex = calculateRiskIndex(pm25)
        const point = { pm25, riskIndex, airQualityPercent: 100 - riskIndex }
        return [...prev.slice(-20), point]
      })
    }, 1500)
    return () => clearInterval(interval)
  }, [])

  const handleModeChange = (mode: 'normal' | 'peak' | 'recover') => {
    modeRef.current = mode
  }

  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={800}>
        Simulación de lecturas PM2.5
      </Typography>
      <SimulationControls onModeChange={handleModeChange} />
      <SimulationLiveChart data={data} />
    </Stack>
  )
}
