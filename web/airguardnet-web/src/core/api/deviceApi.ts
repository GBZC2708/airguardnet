import httpClient from './httpClient'
import type { ApiResponse, Alert, Device, Reading } from '../types'

export const deviceApi = {
  getDevices: async () => {
    const { data } = await httpClient.get<ApiResponse<Device[]>>('/devices')
    return data.data
  },
  getDeviceById: async (id: number) => {
    const { data } = await httpClient.get<ApiResponse<Device>>(`/devices/${id}`)
    return data.data
  },
  getDeviceReadings: async (id: number, params?: Record<string, string | number>) => {
    const { data } = await httpClient.get<ApiResponse<Reading[]>>(`/devices/${id}/readings`, { params })
    return data.data
  },
  getDeviceAlerts: async (id: number, params?: Record<string, string | number>) => {
    const { data } = await httpClient.get<ApiResponse<Alert[]>>(`/devices/${id}/alerts`, { params })
    return data.data
  }
}
