import httpClient from './httpClient'
import type { ApiResponse, Plan, User } from '../types'

export const userApi = {
  getUsers: async (): Promise<User[]> => {
    const { data } = await httpClient.get<ApiResponse<User[]>>('/users')
    return data.data ?? []
  },
  getPlans: async (): Promise<Plan[]> => {
    try {
      const { data } = await httpClient.get<ApiResponse<Plan[]>>('/users/plans')
      return data.data ?? []
    } catch (error) {
      console.warn('Plan endpoint no disponible, usando lista local. TODO: reemplazar cuando exista endpoint real.')
      return [
        { id: 1, name: 'Plan Básico', description: 'Funciones esenciales' },
        { id: 2, name: 'Plan Pro', description: 'Monitoreo avanzado' }
      ]
    }
  }
}
