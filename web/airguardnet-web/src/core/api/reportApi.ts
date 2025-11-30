import httpClient from './httpClient'
import type { ApiResponse, UsageReport } from '../types'

export const reportApi = {
  getUsageReports: async () => {
    const { data } = await httpClient.get<ApiResponse<UsageReport[]>>('/usage-reports')
    return data.data
  }
}
