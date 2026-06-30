import request from '@/utils/request'
import type { LoginRequest, LoginResponse, Result } from '@/types'

export function login(data: LoginRequest) {
  return request.post<Result<LoginResponse>, Result<LoginResponse>>('/auth/login', data)
}

export function changeHeadAdminPassword(oldPassword: string, newPassword: string) {
  return request.put('/auth/headadmin/password', { oldPassword, newPassword })
}
export function changeEmployeePassword(oldPassword: string, newPassword: string) {
  return request.put('/auth/employee/password', { oldPassword, newPassword })
}