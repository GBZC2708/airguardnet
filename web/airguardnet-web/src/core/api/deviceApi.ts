import httpClient from './httpClient'
import type { ApiResponse, Alert, Device, Reading } from '../types'

export const deviceApi = {
  getDevices: async (): Promise<Device[]> => {
    const { data } = await httpClient.get<ApiResponse<Device[]>>('/devices')
    return data.data ?? []
  },
  getDeviceById: async (id: number): Promise<Device | null> => {
    const { data } = await httpClient.get<ApiResponse<Device>>(`/devices/${id}`)
    return data.data ?? null
  },
  getDeviceReadings: async (id: number, params?: Record<string, string | number>): Promise<Reading[]> => {
    const { data } = await httpClient.get<ApiResponse<Reading[]>>(`/devices/${id}/readings`, { params })
    return data.data ?? []
  },
  getDeviceAlerts: async (id: number, params?: Record<string, string | number>): Promise<Alert[]> => {
    const { data } = await httpClient.get<ApiResponse<Alert[]>>(`/devices/${id}/alerts`, { params })
    return data.data ?? []
  }
}
