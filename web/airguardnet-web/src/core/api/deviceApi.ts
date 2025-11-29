import httpClient from './httpClient'
import { Device } from '../types/device'

export const deviceApi = {
  list: async () => {
    const { data } = await httpClient.get('/devices')
    return data as Device[]
  },

  detail: async (id: number) => {
    const { data } = await httpClient.get(`/devices/${id}`)
    return data as Device
  }
}
