import httpClient from './httpClient'
import type { ApiResponse, User } from '../types'

interface LoginPayload {
  email: string
  password: string
}

interface LoginData {
  token: string
  userId: number
  name: string
  email: string
  role: User['role']
  planId: number
}

export const authApi = {
  login: async (payload: LoginPayload) => {
    const { data } = await httpClient.post<ApiResponse<LoginData>>('/login', payload)
    return data
  },
  register: async (payload: Partial<User>) => {
    const { data } = await httpClient.post<ApiResponse<User>>('/register', payload)
    return data
  }
}

export type { LoginData, LoginPayload }
