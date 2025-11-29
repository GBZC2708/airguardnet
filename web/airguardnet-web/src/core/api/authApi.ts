import httpClient from './httpClient'
import { User } from '../types/user'

export const authApi = {
  login: async (email: string, password: string) => {
    const { data } = await httpClient.post('/auth/login', { email, password })
    return data as { token: string; user: User }
  }
}
