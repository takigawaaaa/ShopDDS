import request from '@/utils/request'
import type { Supermarket, Warehouse, Result } from '@/types'

export const listSupermarkets = () => request.get<Result<Supermarket[]>, Result<Supermarket[]>>('/supermarkets')
export const addSupermarket = (data: Supermarket) => request.post('/supermarkets', data)
export const deleteSupermarket = (id: string) => request.delete(`/supermarkets/${id}`)
export const updateSuperName = (id: string, name: string) => request.patch(`/supermarkets/${id}/name`, { name })
export const updateSuperAddress = (id: string, address: string) => request.patch(`/supermarkets/${id}/address`, { address })
export const updateSuperManager = (id: string, employeeId: string) => request.patch(`/supermarkets/${id}/manager`, { employeeId })

export const listWarehouses = () => request.get<Result<Warehouse[]>, Result<Warehouse[]>>('/warehouses')
export const addWarehouse = (data: Warehouse) => request.post('/warehouses', data)
export const deleteWarehouse = (id: string) => request.delete(`/warehouses/${id}`)
export const updateWareName = (id: string, name: string) => request.patch(`/warehouses/${id}/name`, { name })
export const updateWareAddress = (id: string, address: string) => request.patch(`/warehouses/${id}/address`, { address })
export const updateWareManager = (id: string, employeeId: string) => request.patch(`/warehouses/${id}/manager`, { employeeId })