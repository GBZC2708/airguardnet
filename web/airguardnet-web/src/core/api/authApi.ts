import httpClient from './httpClient'
import type { ApiResponse, LoginData, User } from '../types'

interface LoginPayload {
  email: string
  password: string
}

export interface RegisterPayload {
  name: string
  lastName: string
  email: string
  password: string
  role: 'ADMIN' | 'SUPERVISOR' | 'TECNICO' | 'OPERADOR'
  planId: number
}

export const authApi = {
  login: async (payload: LoginPayload): Promise<LoginData> => {
    const { data } = await httpClient.post<ApiResponse<LoginData>>('/login', payload)
    return data.data
  },
  register: async (payload: RegisterPayload): Promise<User> => {
    const { data } = await httpClient.post<ApiResponse<User>>('/register', payload)
    return data.data
  }
}

export type { LoginPayload }
