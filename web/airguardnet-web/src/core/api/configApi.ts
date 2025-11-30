import httpClient from './httpClient'
import type { ApiResponse, ConfigChangeLog, ConfigParameter, SensorConfig } from '../types'

export const configApi = {
  getConfigParameters: async () => {
    const { data } = await httpClient.get<ApiResponse<ConfigParameter[]>>('/config-parameters')
    return data.data
  },
  updateConfigParameter: async (key: string, value: string) => {
    const { data } = await httpClient.put<ApiResponse<ConfigParameter>>(`/config-parameters/${key}`, { value })
    return data.data
  },
  getSensorConfigs: async () => {
    const { data } = await httpClient.get<ApiResponse<SensorConfig[]>>('/sensor-configs')
    return data.data
  },
  updateSensorConfig: async (id: number, payload: Partial<SensorConfig>) => {
    const { data } = await httpClient.put<ApiResponse<SensorConfig>>(`/sensor-configs/${id}`, payload)
    return data.data
  },
  getConfigChangeLogs: async () => {
    const { data } = await httpClient.get<ApiResponse<ConfigChangeLog[]>>('/config-change-logs')
    return data.data
  }
}
