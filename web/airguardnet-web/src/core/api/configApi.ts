import httpClient from './httpClient'

export const configApi = {
  getThresholds: async () => {
    const { data } = await httpClient.get('/config/thresholds')
    return data
  }
}
