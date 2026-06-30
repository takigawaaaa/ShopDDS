import request from '@/utils/request'
import type { SummaryResult, Assign, Allocate, AllocationResult, Result } from '@/types'

// 汇总
export const aggregate = () => request.post('/summary/aggregate')
export const getSummary = () => request.get<Result<SummaryResult>, Result<SummaryResult>>('/summary')
export const clearInventory = () => request.delete('/summary/inventory')
export const clearApplication = () => request.delete('/summary/application')

// 分配
export const executeDistribution = () => request.post<Result<number>, Result<number>>('/distribution/execute')
export const listAssigns = () => request.get<Result<Assign[]>, Result<Assign[]>>('/distribution/assigns')
export const listMyAssigns = () => request.get<Result<Assign[]>, Result<Assign[]>>('/distribution/supermarket/assigns')
export const updateAssign = (supermarketId: string, commodityId: string, assignAmount: number) =>
  request.put('/distribution/assigns', { supermarketId, commodityId, assignAmount })
export const clearAssigns = () => request.delete('/distribution/assigns')

// 调拨
export const executeAllocation = () => request.post<Result<AllocationResult>, Result<AllocationResult>>('/allocation/execute')
export const listAllocations = () => request.get<Result<Allocate[]>, Result<Allocate[]>>('/allocation/allocations')
export const listMyWareAllocations = () => request.get<Result<Allocate[]>, Result<Allocate[]>>('/allocation/warehouse/allocations')
export const listMySuperAllocations = () => request.get<Result<Allocate[]>, Result<Allocate[]>>('/allocation/supermarket/allocations')
export const updateAllocate = (supermarketId: string, warehouseId: string, commodityId: string, allocateAmount: number) =>
  request.put('/allocation/allocations', { supermarketId, warehouseId, commodityId, allocateAmount })
export const clearAllocations = () => request.delete('/allocation/allocations')