import httpClient from './httpClient'
import type { AccessLog, ApiResponse, SystemLog } from '../types'

export const logsApi = {
  getAccessLogs: async (params?: Record<string, string | number>) => {
    const { data } = await httpClient.get<ApiResponse<AccessLog[]>>('/access-logs', { params })
    return data.data
  },
  getSystemLogs: async (params?: Record<string, string | number>) => {
    const { data } = await httpClient.get<ApiResponse<SystemLog[]>>('/system-logs', { params })
    return data.data
  }
}
