import request from '@/utils/request'
import type { DropdownItem, Result } from '@/types'

export const getCommodities = () => request.get<Result<DropdownItem[]>, Result<DropdownItem[]>>('/common/commodities')
export const getWarehouses = () => request.get<Result<DropdownItem[]>, Result<DropdownItem[]>>('/common/warehouses')
export const getSupermarkets = () => request.get<Result<DropdownItem[]>, Result<DropdownItem[]>>('/common/supermarkets')
export const getEmployees = () => request.get<Result<DropdownItem[]>, Result<DropdownItem[]>>('/common/employees')
export const getHeadAdmins = () => request.get<Result<DropdownItem[]>, Result<DropdownItem[]>>('/common/headadmins')
export const getWarehouseManagers = () => request.get<Result<DropdownItem[]>, Result<DropdownItem[]>>('/common/warehouse-managers')
export const getSupermarketManagers = () => request.get<Result<DropdownItem[]>, Result<DropdownItem[]>>('/common/supermarket-managers')