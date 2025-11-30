export interface ApiResponse<T> {
  success: boolean
  message: string | null
  data: T
}

export interface LoginData {
  token: string
  userId: number
  name: string
  email: string
  role: 'ADMIN' | 'SUPERVISOR' | 'TECNICO' | 'OPERADOR'
  planId: number
}

export type UserRole = 'ADMIN' | 'SUPERVISOR' | 'TECNICO' | 'OPERADOR'
export type UserStatus = 'ACTIVE' | 'DISABLED'

export interface User {
  id: number
  name: string
  lastName: string
  email: string
  role: UserRole
  planId: number | null
  status: UserStatus
  lastLoginAt?: string | null
}

export interface Plan {
  id: number
  name: string
  description: string
}

export interface PlanFeature {
  id: number
  featureKey: string
  enabled: boolean
}

export type DeviceStatus = 'ACTIVE' | 'WARNING' | 'CRITICAL' | 'OFFLINE'

export interface Device {
  id: number
  deviceUid: string
  name: string
  status: DeviceStatus
  assignedUserId?: number | null
  lastCommunicationAt?: string | null
  lastBatteryLevel?: number | null
}

export type SensorType = 'PM1' | 'PM25' | 'PM10'

export interface SensorConfig {
  id: number
  sensorType: SensorType
  recommendedMax: number
  criticalThreshold: number
  unit: string
}

export interface Reading {
  id: number
  deviceId: number
  recordedAt: string
  pm1?: number | null
  pm25?: number | null
  pm10?: number | null
  batteryLevel?: number | null
  riskIndex: number
  airQualityPercent: number
}

export type AlertSeverity = 'CRITICAL' | 'HIGH' | 'MEDIUM'
export type AlertStatus = 'PENDING' | 'IN_PROGRESS' | 'RESOLVED'

export interface Alert {
  id: number
  deviceId: number
  readingId: number
  severity: AlertSeverity
  status: AlertStatus
  message: string
  createdAt: string
  resolvedAt?: string | null
}

export type IncidentStatus = 'OPEN' | 'IN_PROGRESS' | 'CLOSED'

export interface Incident {
  id: number
  alertId: number
  description: string
  status: IncidentStatus
  createdById: number
  createdAt: string
  updatedAt?: string
}

export interface UsageReport {
  id: number
  generatedAt: string
  totalUsers: number
  totalDevices: number
  totalReadings: number
  totalAlerts: number
  datosResumen?: any
}

export interface AccessLog {
  id: number
  userId: number
  action: string
  ipAddress: string
  createdAt: string
}

export type SystemLogType = 'ERROR' | 'WARNING' | 'INFO'

export interface SystemLog {
  id: number
  type: SystemLogType
  source: string
  message: string
  createdAt: string
}

export interface ConfigParameter {
  id: number
  key: string
  value: string
  createdAt: string
  updatedAt: string
}

export interface ConfigChangeLog {
  id: number
  parameterKey: string
  oldValue: string
  newValue: string
  changedById: number
  changedAt: string
}

export interface VersionHistory {
  id: number
  versionNumber: string
  description: string
  releasedAt: string
}
