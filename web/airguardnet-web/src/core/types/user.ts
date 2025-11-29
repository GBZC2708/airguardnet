export type UserRole = 'ADMIN' | 'SUPERVISOR' | 'TECNICO' | 'OPERADOR'

export interface User {
  id: number
  name: string
  lastname: string
  email: string
  role: UserRole
  createdAt: string
}
