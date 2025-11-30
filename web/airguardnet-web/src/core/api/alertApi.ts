import httpClient from './httpClient'
import type { Alert, AlertStatus, ApiResponse } from '../types'

export const alertApi = {
  getAlerts: async (params?: Record<string, string | number>) => {
    const { data } = await httpClient.get<ApiResponse<Alert[]>>('/alerts', { params })
    return data.data
  },
  updateAlertStatus: async (id: number, status: AlertStatus) => {
    const { data } = await httpClient.patch<ApiResponse<Alert>>(`/alerts/${id}/status`, { status })
    return data.data
  }
}
