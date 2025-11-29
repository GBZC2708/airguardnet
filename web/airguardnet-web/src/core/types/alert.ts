export interface Alert {
  id: number
  deviceId: number
  timestamp: string
  type: 'PM10' | 'PM25' | 'PM1'
  level: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  value: number
  message: string
}
