import httpClient from './httpClient'
import type { Alert, AlertStatus, ApiResponse } from '../types'

export const alertApi = {
  getAlerts: async (params?: Record<string, string | number>): Promise<Alert[]> => {
    const { data } = await httpClient.get<ApiResponse<Alert[]>>('/alerts', { params })
    return data.data ?? []
  },
  updateAlertStatus: async (id: number, status: AlertStatus): Promise<Alert | null> => {
    const { data } = await httpClient.patch<ApiResponse<Alert>>(`/alerts/${id}/status`, { status })
    return data.data ?? null
  }
}
