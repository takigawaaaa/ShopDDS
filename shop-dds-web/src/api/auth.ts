import request from '@/utils/request'
import type { LoginRequest, LoginResponse, Result } from '@/types'

export interface CaptchaResult {
  image: string
  sessionId: string
  captcha: string
}

export function login(data: LoginRequest) {
  return request.post<Result<LoginResponse>, Result<LoginResponse>>('/auth/login', data)
}

export function getCaptcha() {
  return request.get<Result<CaptchaResult>, Result<CaptchaResult>>('/auth/captcha').then((r) => r.data)
}

export function changeHeadAdminPassword(oldPassword: string, newPassword: string) {
  return request.put('/auth/headadmin/password', { oldPassword, newPassword })
}
export function changeEmployeePassword(oldPassword: string, newPassword: string) {
  return request.put('/auth/employee/password', { oldPassword, newPassword })
}