import request from '@/utils/request'
import type { Employee, HeadAdmin, Result } from '@/types'

export const listHeadAdmins = () => request.get<Result<HeadAdmin[]>, Result<HeadAdmin[]>>('/headadmins')
export const addHeadAdmin = (data: HeadAdmin) => request.post('/headadmins', data)
export const deleteHeadAdmin = (id: string) => request.delete(`/headadmins/${id}`)

export const listEmployees = () => request.get<Result<Employee[]>, Result<Employee[]>>('/employees')
export const addEmployee = (data: Employee) => request.post('/employees', data)
export const deleteEmployee = (id: string) => request.delete(`/employees/${id}`)