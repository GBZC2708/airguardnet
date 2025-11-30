import httpClient from './httpClient'
import type { ApiResponse, VersionHistory } from '../types'

export const versionApi = {
  getVersionHistory: async () => {
    const { data } = await httpClient.get<ApiResponse<VersionHistory[]>>('/version-history')
    return data.data
  }
}
