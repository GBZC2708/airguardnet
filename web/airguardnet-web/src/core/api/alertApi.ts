import httpClient from './httpClient'
import { Alert } from '../types/alert'

export const alertApi = {
  list: async () => {
    const { data } = await httpClient.get('/alerts')
    return data as Alert[]
  }
}
