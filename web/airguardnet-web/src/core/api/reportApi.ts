import httpClient from './httpClient'
import { DailyReport } from '../types/report'

export const reportApi = {
  daily: async () => {
    const { data } = await httpClient.get('/reports/daily')
    return data as DailyReport[]
  }
}
