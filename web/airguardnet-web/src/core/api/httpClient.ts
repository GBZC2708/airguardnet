import axios from 'axios'
import type { ApiResponse } from '../types'

const httpClient = axios.create({
  baseURL: 'http://localhost:8080/api'
})

httpClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('airguardnet_token')
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        localStorage.removeItem('airguardnet_token')
        localStorage.removeItem('airguardnet_user')
        window.location.href = '/login'
      }
      const apiResponse = error.response.data as ApiResponse<unknown>
      if (apiResponse && apiResponse.message) {
        return Promise.reject(new Error(apiResponse.message))
      }
    }
    return Promise.reject(error)
  }
)

export default httpClient
