export interface Device {
  id: number
  serial: string
  name: string
  status: 'ONLINE' | 'OFFLINE'
  battery: number
  lastReadingAt: string
  pm1: number
  pm25: number
  pm10: number
}
