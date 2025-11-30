import httpClient from './httpClient'
import type { ApiResponse, ConfigChangeLog, ConfigParameter, SensorConfig } from '../types'

export const configApi = {
  getConfigParameters: async (): Promise<ConfigParameter[]> => {
    const { data } = await httpClient.get<ApiResponse<ConfigParameter[]>>('/config-parameters')
    return data.data ?? []
  },
  updateConfigParameter: async (key: string, value: string): Promise<ConfigParameter | null> => {
    const { data } = await httpClient.put<ApiResponse<ConfigParameter>>(`/config-parameters/${key}`, { value })
    return data.data ?? null
  },
  getSensorConfigs: async (): Promise<SensorConfig[]> => {
    const { data } = await httpClient.get<ApiResponse<SensorConfig[]>>('/sensor-configs')
    return data.data ?? []
  },
  updateSensorConfig: async (id: number, payload: Partial<SensorConfig>): Promise<SensorConfig | null> => {
    const { data } = await httpClient.put<ApiResponse<SensorConfig>>(`/sensor-configs/${id}`, payload)
    return data.data ?? null
  },
  getConfigChangeLogs: async (): Promise<ConfigChangeLog[]> => {
    const { data } = await httpClient.get<ApiResponse<ConfigChangeLog[]>>('/config-change-logs')
    return data.data ?? []
  }
}
