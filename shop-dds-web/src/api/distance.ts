import request from '@/utils/request'
import type { Distance, Result } from '@/types'

export const listDistances = () => request.get<Result<Distance[]>, Result<Distance[]>>('/distances')
export const addDistance = (data: Distance) => request.post('/distances', data)
export const deleteDistance = (warehouseId: string, supermarketId: string) =>
  request.delete(`/distances/${warehouseId}/${supermarketId}`)
export const deleteAllDistances = () => request.delete('/distances')