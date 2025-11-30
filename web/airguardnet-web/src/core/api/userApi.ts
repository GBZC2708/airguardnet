import httpClient from './httpClient'
import type { ApiResponse, Plan, User, UserStatus } from '../types'

export interface CreateUserPayload {
  name: string
  lastName: string
  email: string
  role: User['role']
  planId: number
  password: string
}

export const userApi = {
  getUsers: async (): Promise<User[]> => {
    const { data } = await httpClient.get<ApiResponse<User[]>>('/users')
    return data.data ?? []
  },
  createUser: async (payload: CreateUserPayload): Promise<User> => {
    const { data } = await httpClient.post<ApiResponse<User>>('/users', payload)
    return data.data
  },
  updateUserStatus: async (id: number, status: UserStatus): Promise<User> => {
    const { data } = await httpClient.patch<ApiResponse<User>>(`/users/${id}/status`, { status })
    return data.data
  },
  getPlans: async (): Promise<Plan[]> => {
    const { data } = await httpClient.get<ApiResponse<Plan[]>>('/users/plans')
    return data.data ?? []
  }
}
